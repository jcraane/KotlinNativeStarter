package nl.jamiecraane.native.api

import nl.jamiecraane.native.domain.Person

interface Api {
    suspend fun retrievePersons(): ApiResponse<List<Person>>
}