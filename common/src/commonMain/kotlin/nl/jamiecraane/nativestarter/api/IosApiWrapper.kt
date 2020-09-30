package nl.jamiecraane.nativestarter.api

import kotlinx.coroutines.*
import nl.jamiecraane.nativestarter.domain.Person
import nl.jamiecraane.nativestarter.domain.Task
import kotlin.coroutines.CoroutineContext

class IosApiWrapper {

    private val scope = MainScope(Dispatchers.Main)

    fun retrievePersons(success: (List<Person>) -> Unit, failure: (Failure<List<Person>>) -> Unit) {
        scope.launch {
            println("Launched with MainScope")
            val response = RealApi().retrievePersons()
            if (response is Success) {
                success(response.data)
            } else if (response is Failure) {
                failure(response)
            }
        }
    }

    fun retrieveTasks(success: (List<Task>) -> Unit, failure: (Failure<List<Task>>) -> Unit) {
        scope.launch {
            val response = RealApi().retrieveTasks()
            if (response is Success) {
                success(response.data)
            } else if (response is Failure) {
                failure(response)
            }
        }
    }
}

class MainScope(private val mainContext: CoroutineContext) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = mainContext + job + exceptionHandler

    internal val job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        nl.jamiecraane.nativestarter.log.error("Error", throwable)
    }
    private val scope = Dispatchers.Main + job + exceptionHandler
}