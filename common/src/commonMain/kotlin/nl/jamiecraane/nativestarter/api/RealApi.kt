package nl.jamiecraane.nativestarter.api

//import io.ktor.client.features.logging.LogLevel
//import io.ktor.client.features.logging.Logger
//import io.ktor.client.features.logging.Logging
//import io.ktor.client.features.logging.SIMPLE
//import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTime
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import io.ktor.http.Url
import io.ktor.http.isSuccess
import kotlinx.io.charsets.Charset
import kotlinx.serialization.list
import nl.jamiecraane.nativestarter.domain.Message
import nl.jamiecraane.nativestarter.domain.Person
import nl.jamiecraane.nativestarter.domain.Task
import nl.jamiecraane.nativestarter.log.log

class RealApi : Api {
    private val client = HttpClient() {

    }

    override suspend fun retrievePersons(): ApiResponse<List<Person>> {
        log("RETRIEVEPERSONS")
        return withinTryCatch<List<Person>> {
            val datetime = DateTime(2019, 6, 3)
            println("DATETIME = $datetime")

            val response = client.get<HttpResponse> {
                url(Url("http://192.168.1.241:2500/persons"))
            }

            if (response.status.isSuccess()) {
                Success(
                    jsonParser.parse(
                        Person.serializer().list,
                        response.readText(Charset.forName("UTF-8"))
                    )
                )
            } else {
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
    } catch (exception: Exception) {
        return Failure(500, exception.message)
    }
}