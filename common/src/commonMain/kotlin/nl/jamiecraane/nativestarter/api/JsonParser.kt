package nl.jamiecraane.nativestarter.api

import kotlinx.serialization.json.Json

val jsonParser = Json {
    ignoreUnknownKeys = true
    isLenient = true
}