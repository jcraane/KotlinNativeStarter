package nl.jamiecraane.nativestarter.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.jamiecraane.nativestarter.api.RealApi
import nl.jamiecraane.nativestarter.api.Success
import nl.jamiecraane.nativestarter.domain.Person

class PersonsViewModel {
    val realApi = RealApi()

    var persons: Flow<List<Person>> = flow {
        val response= realApi.retrievePersons()
        if (response is Success) {
            emit(response.data)
        }
    }
}