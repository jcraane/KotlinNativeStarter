package nl.jamiecraane.nativestarter.state

expect fun Any.ensureNeverFrozen()
expect val Any.isFrozen: Boolean
expect fun <T> T.freeze(): T