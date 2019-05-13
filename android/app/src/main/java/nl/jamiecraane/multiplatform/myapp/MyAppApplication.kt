package nl.jamiecraane.multiplatform.myapp

import android.app.Application
import nl.jamiecraane.multiplatform.myapp.koin.modules
import org.koin.android.ext.android.startKoin

class MyAppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, modules)
    }
}