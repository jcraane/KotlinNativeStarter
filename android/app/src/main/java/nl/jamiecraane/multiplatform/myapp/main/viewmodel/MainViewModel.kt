package nl.jamiecraane.multiplatform.myapp.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soywiz.klock.DateTime
//import com.soywiz.klock.DateTime
//import com.soywiz.klock.KlockLocale
//import com.soywiz.klock.format
//import com.soywiz.klock.locale.dutch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jamiecraane.nativestarter.api.Failure
import nl.jamiecraane.nativestarter.api.RealApi
import nl.jamiecraane.nativestarter.api.Success
import nl.jamiecraane.nativestarter.domain.Person
import nl.jamiecraane.nativestarter.domain.Task

class MainViewModel : ViewModel() {
    private val api = RealApi()

    val persons: MutableLiveData<List<Person>> = MutableLiveData()
    val tasks: MutableLiveData<List<Task>> = MutableLiveData()
    val message = MutableLiveData<String>()
    val errorText = MutableLiveData("")
    val currentTime = MutableLiveData<String>()

    init {
//        todo test retrieve async
        viewModelScope.launch {
            retrievePersons()
            retrieveTasks()
        }

        val now = DateTime.now()
        println("DATETIME IN ANDROID $now")
//        currentTime.value = KlockLocale.dutch.formatDateShort.format(now)
    }

    private suspend fun retrievePersons() {
        val response = withContext(Dispatchers.IO) {
            api.retrievePersons()
        }

        when (response) {
            is Success -> {
                persons.value = response.data
            }
            is Failure -> errorText.value = "Error"
        }
    }

    private suspend fun retrieveTasks() {
        val tasksResponse = withContext(Dispatchers.IO) {
            api.retrieveTasks()
        }

        if (tasksResponse is Success) {
            tasks.value = tasksResponse.data
        }
    }
}

class Item(val person: Person) {

}