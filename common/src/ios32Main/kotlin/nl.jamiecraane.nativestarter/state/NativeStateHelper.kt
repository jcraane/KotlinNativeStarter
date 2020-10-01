package nl.jamiecraane.nativestarter.state

import kotlin.native.concurrent.ensureNeverFrozen
import kotlin.native.concurrent.freeze
import kotlin.native.concurrent.isFrozen

actual fun Any.ensureNeverFrozen() = this.ensureNeverFrozen()
actual val Any.isFrozen: Boolean
    get() = this.isFrozen
actual fun <T> T.freeze(): T = this.freeze()