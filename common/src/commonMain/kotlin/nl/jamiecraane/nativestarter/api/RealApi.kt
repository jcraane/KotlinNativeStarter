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
import nl.jamiecraane.nativestarter.log.log

class RealApi : Api {
    private val client = HttpClient() {

    }

    override suspend fun retrievePersons(): ApiResponse<List<Person>> {
        log("RETRIEVEPERSONS")
        val datetime = DateTime(2019, 6, 3)
        println("DATETIME = $datetime")

        val response = client.get<HttpResponse> {
            url(Url("http://10.0.2.2:2500/persons"))
        }

        return if (response.status.isSuccess()) {
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

    override suspend fun sayHello(): ApiResponse<String> {
        log("SayHello")
        val datetime = DateTime(2019, 6, 3)
        println("DATETIME = $datetime")

        val response = client.get<HttpResponse> {
            url(Url("http://192.168.1.241:2500/sayhello"))
        }

        return if (response.status.isSuccess()) {
            Success(
                jsonParser.parse(
                    Message.serializer(),
                    response.readText(Charset.forName("UTF-8"))
                ).message
            )
        } else {
            Failure(response.status.value, "Error")
        }
    }
}
