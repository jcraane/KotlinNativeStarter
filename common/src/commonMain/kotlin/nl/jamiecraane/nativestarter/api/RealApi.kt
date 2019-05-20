package nl.jamiecraane.nativestarter.api

import io.ktor.client.HttpClient
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import io.ktor.http.Url
import io.ktor.http.isSuccess
import kotlinx.io.charsets.Charset
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import nl.jamiecraane.nativestarter.domain.Person

class RealApi : Api {
    private val client = HttpClient() {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
//        install(JsonFeature) {
//            jsonfeature does not work nicely yet with list, following error:  Fail to run receive pipeline: kotlinx.serialization.SerializationException: Can't locate argument-less serializer for class kotlin.collections.List. For generic classes, such as lists, please provide serializer explicitly.
//            serializer = KotlinxSerializer(Json.nonstrict)
//        }
    }


    /* {
       install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
    }*/

    override suspend fun retrievePersons(): ApiResponse<List<Person>> {
        val response = client.get<HttpResponse> {
            url(Url("http://192.168.1.48:2500/persons"))
        }

        return if (response.status.isSuccess()) {
            Success(
                Json.nonstrict.parse(
                    Person.serializer().list,
                    response.readText(Charset.forName("UTF-8"))
                )
            )
        } else {
            Failure(response.status.value, "Error")
        }
    }
}