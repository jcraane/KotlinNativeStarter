package nl.jamiecraane.nativestarter.api

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.jamiecraane.nativestarter.domain.Person
import nl.jamiecraane.nativestarter.domain.Task

internal expect val ApplicationDispatcher: CoroutineDispatcher

class IosApiWrapper {
    fun retrievePersons(success: (List<Person>) -> Unit, failure: (Failure<List<Person>>) -> Unit) {
        GlobalScope.launch(ApplicationDispatcher) {
            val response = RealApi().retrievePersons()
            if (response is Success) {
                success(response.data)
            } else if (response is Failure) {
                failure(response)
            }
        }
    }

    fun retrieveTasks(success: (List<Task>) -> Unit, failure: (Failure<List<Task>>) -> Unit) {
        println("Test")
        GlobalScope.launch(ApplicationDispatcher) {
            val response = RealApi().retrieveTasks()
            if (response is Success) {
                success(response.data)
            } else if (response is Failure) {
                failure(response)
            }
        }
    }
}