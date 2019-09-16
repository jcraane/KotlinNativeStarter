package nl.jamiecraane.multiplatform.myapp

import android.app.Application
import nl.jamiecraane.multiplatform.myapp.koin.modules
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class MyAppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, modules)
        Timber.plant(Timber.DebugTree())
    }
}