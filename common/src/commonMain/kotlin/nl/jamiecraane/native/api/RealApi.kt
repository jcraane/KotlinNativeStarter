package nl.jamiecraane.native.api

import io.ktor.client.HttpClient
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.response.HttpResponse
import io.ktor.http.Url
import nl.jamiecraane.native.domain.Person

class RealApi : Api {
    private val client = HttpClient {
       install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
    }

    override suspend fun retrievePersons(): List<Person> {
        val response = client.get<HttpResponse> {
            url(Url("http://10.128.238.209:2500/persons"))
        }

        return emptyList()
        /*if (response.status.isSuccess()) {

        }*/
    }
}