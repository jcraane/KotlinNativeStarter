package nl.jamiecraane.nativestarter.api

import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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
            realApi.testFLow()
                .take(1)
                .collect {
                    success(it)

                    realApi.setValue("Value from wrapper")
                }
        }
    }

    fun complexFlow(): CFlow<String> {
        val responseFlow: Flow<String> = flow {
            scope.launch {
                realApi.testFLow().take(1).transformLatest {
                    emit(it)
                }
            }
        }
        return responseFlow.wrap()
    }

}

fun <T> Flow<T>.wrap(): CFlow<T> =
    CFlow(this)

class CFlow<T>( val origin: Flow<T>) : Flow<T> by origin {
    private val scope = MainScope()
    fun watch(block: (T) -> Unit): Closeable {
        val job = Job(/*ConferenceService.coroutineContext[Job]*/)

        onEach {
            block(it)
        }.launchIn(scope)

        return object : Closeable {
            override fun close() {
                job.cancel()
            }
        }
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