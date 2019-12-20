package nl.jamiecraane.nativestarter.api

import nl.jamiecraane.nativestarter.domain.Person
import nl.jamiecraane.nativestarter.domain.Task

interface Api {
    suspend fun retrievePersons(): ApiResponse<List<Person>>
    suspend fun retrieveTasks(): ApiResponse<List<Task>>
}