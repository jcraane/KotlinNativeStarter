package nl.jamiecraane.nativestarter.api

import com.soywiz.klock.DateTime
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.response.readText
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.http.Url
import io.ktor.http.isSuccess
import io.ktor.utils.io.charsets.Charset
import kotlinx.serialization.builtins.list
import nl.jamiecraane.nativestarter.domain.Person
import nl.jamiecraane.nativestarter.domain.Task
import nl.jamiecraane.nativestarter.log.info
import nl.jamiecraane.nativestarter.log.ktor.logging.LogLevel
import nl.jamiecraane.nativestarter.log.ktor.logging.Logger
import nl.jamiecraane.nativestarter.log.ktor.logging.Logging
import nl.jamiecraane.nativestarter.log.ktor.logging.SIMPLE

//Mockoon does not have good response times at the moment, see: https://github.com/mockoon/mockoon/issues/48
class RealApi : Api {
    private val client = HttpClient {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
    }
    override suspend fun retrievePersons(): ApiResponse<List<Person>> {
        println("Persons from common3")
        return withinTryCatch<List<Person>> {
            val start = DateTime.nowUnixLong()
            val response = client.get<HttpResponse> {
//                url(Url("https://www.test.nl/persons"))
//                url(Url("http://10.0.2.2:2500/persons"))
                url(Url("http://localhost:2500/persons"))
            }
            val end = DateTime.nowUnixLong()
            println("End persons service call = ${end - start}")

            if (response.status.isSuccess()) {
                Success(
                    jsonParser.parse(
                        Person.serializer().list,
                        response.readText(Charset.forName("UTF-8"))
                    )
                )
            } else {
                println("is Failure")
                Failure(response.status.value, "Error")
            }
        }
    }

    override suspend fun retrieveTasks(): ApiResponse<List<Task>> {
        println("RETRIEVE TASKS")
        return withinTryCatch<List<Task>> {
            val response = client.get<HttpResponse> {
                url(Url("http://192.168.1.241:2500/tasks"))
            }
            if (response.status.isSuccess()) {
                Success(
                    jsonParser.parse(
                        Task.serializer().list,
                        response.readText(Charset.forName("UTF-8"))
                    )
                )
            } else {
                Failure(response.status.value, "Error")
            }
        }
    }
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