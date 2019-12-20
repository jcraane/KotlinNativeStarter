package nl.jamiecraane.nativestarter.api

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.jamiecraane.nativestarter.domain.Person

internal expect val ApplicationDispatcher: CoroutineDispatcher

//todo can we make this generic? Or at least use live templates for this
class IosApiWrapper {
    fun retrievePersons(success: (List<Person>) -> Unit, failure: (Throwable?) -> Unit) {
        GlobalScope.launch(ApplicationDispatcher) {
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