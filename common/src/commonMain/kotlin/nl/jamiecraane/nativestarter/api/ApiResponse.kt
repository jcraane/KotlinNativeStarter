package nl.jamiecraane.nativestarter.api

sealed class ApiResponse<out T>

class Success<out T>(val data: T) : ApiResponse<T>(){
    override fun toString(): String {
        return "Response data: $data"
    }
}

class Failure<out T>(
    val status: Int,
    val message: String? = null) : ApiResponse<T>() {
}
