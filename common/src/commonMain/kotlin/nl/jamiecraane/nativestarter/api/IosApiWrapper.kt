package nl.jamiecraane.nativestarter.api

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.jamiecraane.nativestarter.domain.Person

internal expect val ApplicationDispatcher: CoroutineDispatcher

class IosApiWrapper {
    fun retrievePersons(success: (List<Person>) -> Unit, failure: (Throwable?) -> Unit) {
        GlobalScope.launch {
            try {
                val response = RealApi().retrievePersons()
                if (response is Success) {
                    success(response.data)
                } else {
                    failure(null)
                }
            } catch (e: Exception) {
                failure(e)
            }
        }
    }
}