package nl.jamiecraane.nativestarter.log.ktor.logging

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.features.observer.ResponseHandler
import io.ktor.client.features.observer.ResponseObserver
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.charset
import io.ktor.http.content.OutgoingContent
import io.ktor.http.contentType
import io.ktor.util.AttributeKey
import io.ktor.utils.io.ByteChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.close
import io.ktor.utils.io.core.String
import io.ktor.utils.io.core.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.jamiecraane.nativestarter.log.info


/**
 * [HttpClient] logging feature.
 */
class Logging(
    val logger: Logger,
    var level: LogLevel
) {
    /**
     * [Logging] feature configuration
     */
    class Config {
        /**
         * [Logger] instance to use
         */
        var logger: Logger = Logger.SIMPLE

        /**
         * log [LogLevel]
         */
        var level: LogLevel = LogLevel.HEADERS
    }

    private suspend fun logRequest(request: HttpRequestBuilder) {
        if (level.info) {
            info("REQUEST: ${request.url.buildString()}")
            info("METHOD: ${request.method}")
        }
        if (level.headers) logHeaders(request.headers.entries())
        if (level.body) logRequestBody(request.body as OutgoingContent)
    }

    private suspend fun logResponse(response: HttpResponse) {
        if (level == LogLevel.NONE) {
            response.content.discard(Long.MAX_VALUE)
            return
        }

        info("_______ begin ________")
        info("RESPONSE: ${response.status}")
        info("METHOD: ${response.call.request.method}")
        info("FROM: ${response.call.request.url}")

        if (level.headers) logHeaders(response.headers.entries())
        if (level.body) {
            logResponseBody(response.contentType(), response.content)
        } else {
            response.content.discard(Long.MAX_VALUE)
        }
        info("_______ end ________")
    }

    private fun logHeaders(headersMap: Set<Map.Entry<String, List<String>>>) {
        with(logger) {
            info("HEADERS")

            headersMap.forEach { (key, values) ->
                info("-> $key: ${values.joinToString("; ")}")
            }
        }
    }

    private suspend fun logResponseBody(contentType: ContentType?, content: ByteReadChannel) {
        with(logger) {
            info("BODY Content-Type: $contentType")
            info("BODY START")
            val message = content.readText(contentType?.charset() ?: Charsets.UTF_8)
            info(message)
            info("BODY END")
        }
    }

    private suspend fun logRequestBody(content: OutgoingContent) {
        with(logger) {
            info("BODY Content-Type: ${content.contentType}")

            val charset = content.contentType?.charset() ?: Charsets.UTF_8

            val text = when (content) {
                is OutgoingContent.WriteChannelContent -> {
                    val textChannel = ByteChannel()
                    GlobalScope.launch(Dispatchers.Unconfined) {
                        content.writeTo(textChannel)
                        textChannel.close()
                    }
                    textChannel.readText(charset)
                }
                is OutgoingContent.ReadChannelContent -> {
                    content.readFrom().readText(charset)
                }
                is OutgoingContent.ByteArrayContent -> String(
                    content.bytes(),
                    charset = charset
                )
                else -> null
            }
            info("BODY START")
            text?.let { info(it) }
            info("BODY END")
        }
    }

    companion object : HttpClientFeature<Config, Logging> {
        override val key: AttributeKey<Logging> = AttributeKey("ClientLogging")

        override fun prepare(block: Config.() -> Unit): Logging {
            val config = Config().apply(block)
            return Logging(config.logger, config.level)
        }

        override fun install(feature: Logging, scope: HttpClient) {
            scope.sendPipeline.intercept(HttpSendPipeline.Before) {
                try {
                    feature.logRequest(context)
                } catch (_: Throwable) {
                }
            }

            val observer: ResponseHandler = {
                try {
                    feature.logResponse(it)
                } catch (_: Throwable) {
                }
            }

            ResponseObserver.install(ResponseObserver(observer), scope)
        }
    }
}

private suspend inline fun ByteReadChannel.readText(charset: Charset): String {
    val packet = readRemaining(Long.MAX_VALUE, 0)
    return packet.readText(charset = charset)
}
