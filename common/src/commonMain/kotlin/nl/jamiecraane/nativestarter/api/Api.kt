package nl.jamiecraane.nativestarter.api

import nl.jamiecraane.nativestarter.domain.Person

interface Api {
    suspend fun retrievePersons(): ApiResponse<List<Person>>
    suspend fun sayHello(): ApiResponse<String>
}