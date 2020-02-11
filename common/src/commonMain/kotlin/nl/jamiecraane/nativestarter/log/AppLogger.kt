package nl.jamiecraane.nativestarter.log

interface IOSLoggingDelegate {
    val isDebugLoggingEnabled: Boolean
    fun error(message: String)
    fun warn(message: String)
    fun debug(message: String)
    fun info(message: String)
}

var iosLoggingDelegate: IOSLoggingDelegate? = null

expect fun error(message: String, error: Throwable?, vararg args: String)
expect fun warn(message: String, error: Throwable? = null, vararg args: String = emptyArray())
expect fun debug(message: String, vararg args: String = emptyArray())
expect fun info(message: String, vararg args: String = emptyArray())