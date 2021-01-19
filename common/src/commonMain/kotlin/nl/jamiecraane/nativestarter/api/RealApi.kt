package nl.jamiecraane.nativestarter.api

import com.soywiz.klock.DateTime
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.http.Url
import io.ktor.http.isSuccess
import io.ktor.utils.io.charsets.Charset
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.builtins.ListSerializer
import nl.jamiecraane.nativestarter.domain.Person
import nl.jamiecraane.nativestarter.domain.Task
import nl.jamiecraane.nativestarter.log.ktor.logging.LogLevel
import nl.jamiecraane.nativestarter.log.ktor.logging.Logger
import nl.jamiecraane.nativestarter.log.ktor.logging.Logging
import nl.jamiecraane.nativestarter.log.ktor.logging.SIMPLE
import nl.jamiecraane.nativestarter.state.ensureNeverFrozen
import nl.jamiecraane.nativestarter.state.isFrozen
import sample.isIos

//Mockoon does not have good response times at the moment, see: https://github.com/mockoon/mockoon/issues/48
class RealApi : Api {
    private val client = HttpClient {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
    }

//    variable property for demonstration purposes
    var id: Long = 0

    init {
//        ensureNeverFrozen()
    }

    private fun getBaseUrl() = if (isIos()) "http://localhost:2500" else "http://10.0.2.2:2500"

    private val mutex = Mutex()

    override suspend fun retrievePersons(): ApiResponse<List<Person>> {
        println("Persons from common2")
//        return mutex.withLock {
            return withinTryCatch<List<Person>> {
                val start = DateTime.nowUnixLong()
                val response = client.get<HttpResponse> {
                    mutex.withLock {
                        url(Url("${getBaseUrl()}/persons"))
                    }
                }
                val end = DateTime.nowUnixLong()
                println("End persons service call = ${end - start}")

                println("Reponse.isFrozen ${response.isFrozen}")
                println("api.isFrozen ${this.isFrozen}")
                if (response.status.isSuccess()) {
                    val persons = jsonParser.decodeFromString(
                        ListSerializer(Person.serializer()),
                        response.readText(Charset.forName("UTF-8"))
                    )
//                    persons[0].realApi = this
//                    test mutation
//                    persons[1].age = 200
                    Success(persons)
                } else {
                    println("is Failure")
                    Failure(response.status.value, "Error")
                }
            }
//        }
    }

    override suspend fun retrieveTasks(): ApiResponse<List<Task>> {
        println("RETRIEVE TASKS")
        return withinTryCatch<List<Task>> {
            val response = client.get<HttpResponse> {
                url(Url("${getBaseUrl()}/tasks"))
            }
            if (response.status.isSuccess()) {
                Success(
                    jsonParser.decodeFromString(
                        ListSerializer(Task.serializer()),
                        response.readText(Charset.forName("UTF-8"))
                    )
                )
            } else {
                Failure(response.status.value, "Error")
            }
        }
    }

    var f = MutableStateFlow("Hello")

    override fun setValue(value: String) {
        f.value = value
    }

    override suspend fun testFLow(): Flow<String> {
        println("Took value from flow")
        return f
    }

    override suspend fun sendToChannelAndClose() {
        channel.send("First element to channel")
        channel.close()
        channel.offer("1")
        println("CHANNEL = $channel")
    }

    override val channel: Channel<String> = Channel()
}

suspend fun <T> withinTryCatch(block: suspend () -> ApiResponse<T>): ApiResponse<T> {
    try {
        return block()
    } catch (exception: Throwable) {
        return if (exception.message?.contains("Code=-1009") == true) { //Is Network down, code..
            Failure(-1009, "Error")
        } else {
            Failure(500, exception.message) //Unknown failure
        }
    }
}