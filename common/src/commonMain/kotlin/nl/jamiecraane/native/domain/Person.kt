package nl.jamiecraane.native.domain

import kotlinx.serialization.Serializable

@Serializable
data class Person(val firstName: String, val lastName: String) {

}