package nl.jamiecraane.nativestarter.domain

import kotlinx.serialization.Serializable

@Serializable
data class Task(val id: Int, val name: String)