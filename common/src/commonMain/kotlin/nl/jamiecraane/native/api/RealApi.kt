package nl.jamiecraane.native.api

import io.ktor.client.HttpClient
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.logging.SIMPLE
import nl.jamiecraane.native.domain.Person

class RealApi : Api {
    private val httpClient = HttpClient {
       install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
    }

    override suspend fun retrievePersons(): List<Person> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}