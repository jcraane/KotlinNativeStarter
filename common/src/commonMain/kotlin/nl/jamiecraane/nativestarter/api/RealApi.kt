package nl.jamiecraane.nativestarter.api

import io.ktor.client.HttpClient
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
    private val client = HttpClient() /* {
       install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
    }*/

    override suspend fun retrievePersons(): ApiResponse<List<Person>> {
        val response = client.get<HttpResponse> {
            url(Url("http://10.128.238.209:2500/persons"))
        }

        return if (response.status.isSuccess()) {
            Success(
                Json.parse(
                    Person.serializer().list,
                    response.readText(Charset.forName("UTF-8"))
                )
            )
        } else {
            Failure(response.status.value, "Error")
        }
    }
}