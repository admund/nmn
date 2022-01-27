package me.admund.nmn

import android.app.Application
import me.admund.nmn.di.appModule
import me.admund.nmn.di.retrofitModule
import me.admund.nmn.di.roomModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class NmnApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NmnApplication)
            modules(appModule, roomModule, retrofitModule)
        }
    }
}
