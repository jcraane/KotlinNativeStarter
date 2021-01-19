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
import kotlinx.coroutines.flow.Flow
import nl.jamiecraane.nativestarter.domain.Person
import nl.jamiecraane.nativestarter.domain.Task
import nl.jamiecraane.nativestarter.log.info
import nl.jamiecraane.nativestarter.log.ktor.logging.LogLevel
import nl.jamiecraane.nativestarter.log.ktor.logging.Logger
import nl.jamiecraane.nativestarter.log.ktor.logging.Logging
import nl.jamiecraane.nativestarter.log.ktor.logging.SIMPLE

//Mockoon does not have good response times at the moment, see: https://github.com/mockoon/mockoon/issues/48
class MockApi : Api {
    override suspend fun retrievePersons(): ApiResponse<List<Person>> {
        return Success(
            listOf(
                Person("Jan", "Janssen"),
                Person("John", "Smith")
            )
        )
    }

    override suspend fun retrieveTasks(): ApiResponse<List<Task>> {
        TODO("Not yet implemented for mock")
    }

    override suspend fun testFLow(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun setValue(value: String) {
        TODO("Not yet implemented")
    }
}
