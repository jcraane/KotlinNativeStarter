package nl.jamiecraane.nativestarter.domain

import kotlinx.serialization.Serializable

@Serializable
data class Person(val firstName: String, val lastName: String) {

}