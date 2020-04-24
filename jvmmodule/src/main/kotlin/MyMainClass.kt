import kotlinx.coroutines.runBlocking
import nl.jamiecraane.nativestarter.api.MockApi
import nl.jamiecraane.nativestarter.api.RealApi

fun main(args: Array<String>) = runBlocking {
    println("Hello")
    val persons = MockApi().retrievePersons()
    println(persons)
}
