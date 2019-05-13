package nl.jamiecraane.multiplatform.myapp.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jamiecraane.native.api.Failure
import nl.jamiecraane.native.api.RealApi
import nl.jamiecraane.native.api.Success
import nl.jamiecraane.native.domain.Person

class MainViewModel : ViewModel() {
    private val api = RealApi()

//    todo why does this declaration generate a compiler error in de Kapt stage?
//    val persons: MutableLiveData<List<Item>> = MutableLiveData(emptyList())
    val persons: MutableLiveData<List<Item>> = MutableLiveData(emptyList())
    val errorText = MutableLiveData("")

    init {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                api.retrievePersons()
            }

            when (response) {
                is Success -> {
                    persons.value = response.data.map { Item(it) }
//                    println("PERSONS ${response.data}")
                }
                is Failure -> errorText.value = "Error"
            }
        }
    }
}

class Item(val person: Person) {

}