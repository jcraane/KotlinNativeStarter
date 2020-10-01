package nl.jamiecraane.nativestarter.state

actual fun Any.ensureNeverFrozen() {
    // Ignored
}
actual val Any.isFrozen: Boolean
    get() = false

actual fun <T> T.freeze(): T {
    // Ignored
}