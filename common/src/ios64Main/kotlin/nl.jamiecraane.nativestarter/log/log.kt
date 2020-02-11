package nl.jamiecraane.nativestarter.log

actual fun error(message: String, error: Throwable?, vararg args: String) {
    iosLoggingDelegate?.error(message)
}

actual fun warn(message: String, error: Throwable?, vararg args: String) {
    iosLoggingDelegate?.warn(message)
}

actual fun debug(message: String, vararg args: String) {
    iosLoggingDelegate?.debug(message)
}

actual fun info(message: String, vararg args: String) {
    iosLoggingDelegate?.info(message)
}