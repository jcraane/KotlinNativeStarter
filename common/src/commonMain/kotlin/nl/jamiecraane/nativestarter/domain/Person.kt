package nl.jamiecraane.nativestarter.domain

import kotlinx.serialization.Serializable
import nl.jamiecraane.nativestarter.state.ensureNeverFrozen

@Serializable
data class Person(val firstName: String, val lastName: String) {
    init {
//        ensureNeverFrozen()
    }

    var age: Int = 0
    val fullname: String
        get() {
            return "$firstName $lastName"
        }
}