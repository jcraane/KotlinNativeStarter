package nl.jamiecraane.nativestarter.api

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.jamiecraane.nativestarter.domain.Person
import nl.jamiecraane.nativestarter.domain.Task

internal expect val ApplicationDispatcher: CoroutineDispatcher

class IosApiWrapper {
    fun retrievePersons(success: (List<Person>) -> Unit, failure: (Throwable?) -> Unit) {
        GlobalScope.launch(ApplicationDispatcher) {
            execute(success, failure) {
                RealApi().retrievePersons()
            }
        }
    }

    fun retrieveTasks(success: (List<Task>) -> Unit, failure: (Throwable?) -> Unit) {
        GlobalScope.launch(ApplicationDispatcher) {
            execute(success, failure) {
                RealApi().retrieveTasks()
            }
        }
    }

    private suspend fun <T> execute(success: (T) -> Unit, failure: (Throwable?) -> Unit, block: suspend () -> ApiResponse<T>) {
        try {
            val response = block()
            if (response is Success) {
                success(response.data)
            } else {
                failure(null)
            }
        } catch (exception: Exception) {
            failure(exception)
        }
    }
}