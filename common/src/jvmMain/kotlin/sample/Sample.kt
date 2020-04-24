package sample

actual class Sample actual constructor() {
    actual fun checkMe(): Int {
        TODO("Not yet implemented")
    }
}

actual object Platform {
    actual val name: String
        get() = TODO("Not yet implemented")
}