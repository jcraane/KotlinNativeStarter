package nl.jamiecraane.nativestarter.log

import timber.log.Timber

actual fun log(msg: String) {
    Timber.e(msg)
}