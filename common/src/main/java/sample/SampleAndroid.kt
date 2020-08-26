package sample

actual class Sample {
    actual fun checkMe() = 8
}

actual object Platform {
    actual val name: String = "Android"
}

actual fun isIos() = false