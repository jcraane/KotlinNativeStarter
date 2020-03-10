package nl.jamiecraane.nativestarter.api

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

val jsonParser = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true, isLenient = true))