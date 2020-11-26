import kotlinx.coroutines.runBlocking
import nl.jamiecraane.nativestarter.api.MockApi

fun main() = runBlocking {
    println("Hello")
    val persons = MockApi().retrievePersons()
    println(persons)
}
