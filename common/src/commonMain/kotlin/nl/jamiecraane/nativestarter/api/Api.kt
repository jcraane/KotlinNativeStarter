package nl.jamiecraane.nativestarter.api

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import nl.jamiecraane.nativestarter.domain.Person
import nl.jamiecraane.nativestarter.domain.Task

interface Api {
    suspend fun retrievePersons(): ApiResponse<List<Person>>
    suspend fun retrieveTasks(): ApiResponse<List<Task>>
    suspend fun testFLow(): Flow<String>
    fun setValue(value: String)
    suspend fun sendToChannelAndClose()
    val channel: Channel<String>
}