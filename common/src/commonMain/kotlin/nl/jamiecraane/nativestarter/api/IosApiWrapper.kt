package nl.jamiecraane.nativestarter.api

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import nl.jamiecraane.nativestarter.domain.Person
import nl.jamiecraane.nativestarter.domain.Task
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class IosApiWrapper {

    private val scope = MainScope(Dispatchers.Main)
    val realApi = RealApi()

    fun retrievePersons(success: (List<Person>) -> Unit, failure: (Failure<List<Person>>) -> Unit) {
        scope.launch {
            println("Launched with MainScope")
            val response = realApi.retrievePersons()
//            realApi.id = Random.nextLong()
            if (response is Success) {
                success(response.data)
            } else if (response is Failure) {
                failure(response)
            }
        }
    }

    fun retrieveTasks(success: (List<Task>) -> Unit, failure: (Failure<List<Task>>) -> Unit) {
        scope.launch {
            val response = realApi.retrieveTasks()
            if (response is Success) {
                success(response.data)
            } else if (response is Failure) {
                failure(response)
            }
        }
    }

    fun testFlow(success: (String) -> Unit) {
        scope.launch {
            success(realApi.testFLow().first())
            /*    .collect {
                    success(it)
                    realApi.setValue("Value from wrapper")
                }*/
        }
    }

    fun sendToChannel() {
        scope.launch {
            realApi.sendToChannelAndClose()
        }
    }

    fun getFromChannel(success: (String) -> Unit) {
        scope.launch {
            val value = realApi.channel.receive()
            success(value)
        }
    }

    suspend fun getFLow(): Flow<String> {
        return realApi.testFLow()
    }
}

class MainScope(private val mainContext: CoroutineContext) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = mainContext + job + exceptionHandler

    internal val job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("Error occurred: ${throwable.message}")
    }
    private val scope = Dispatchers.Main + job + exceptionHandler
}