package nl.jamiecraane.nativestarter.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nl.jamiecraane.nativestarter.api.RealApi
import nl.jamiecraane.nativestarter.state.ensureNeverFrozen

@Serializable
data class Person(val firstName: String, val lastName: String) {
//    This reference to RealApi is just for demonstration purposes
    @Transient
    var realApi: RealApi? = null

    init {
//        ensureNeverFrozen()
    }

    var age: Int = 0
    val fullname: String
        get() {
            return "$firstName $lastName"
        }
}