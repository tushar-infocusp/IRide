package com.example.iride

import android.app.Application
import com.example.iride.data.AppContextHolder
import com.example.iride.di.androidModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppContextHolder.context = applicationContext

        startKoin {
            androidLogger()
            androidContext(this@BaseApplication)
            modules(androidModule)
        }
    }
}