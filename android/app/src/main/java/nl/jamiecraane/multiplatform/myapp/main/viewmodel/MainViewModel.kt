package nl.jamiecraane.multiplatform.myapp.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jamiecraane.nativestarter.api.Failure
import nl.jamiecraane.nativestarter.api.RealApi
import nl.jamiecraane.nativestarter.api.Success
import nl.jamiecraane.nativestarter.domain.Person

class MainViewModel : ViewModel() {
    private val api = RealApi()

    val persons: MutableLiveData<List<Person>> = MutableLiveData()
    val errorText = MutableLiveData("")

    init {
        viewModelScope.launch {
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
    }
}

class Item(val person: Person) {

}