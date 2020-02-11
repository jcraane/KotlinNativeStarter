package nl.jamiecraane.nativestarter.log

import timber.log.Timber

actual fun error(message: String, error: Throwable?, vararg args: String) {
    Timber.e(error, message)
}

actual fun warn(message: String, error: Throwable?, vararg args: String) {
    Timber.w(error, message, args)
}

actual fun debug(message: String, vararg args: String) {
    Timber.d(message, args)
}

actual fun info(message: String, vararg args: String) {
    Timber.i(message, args)
}