package sample

expect class Sample() {
    fun checkMe(): Int
}

expect object Platform {
    val name: String
}

expect fun isIos(): Boolean

fun hello(): String = "Hello from ${Platform.name}"

class Proxy {
    fun proxyHello() = hello()
}
