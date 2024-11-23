package com.vazh2100.geoeventapp

import android.app.Application
import com.vazh2100.geoeventapp.domain.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(
                listOf(
                    appModule
                )
            )
        }
    }
}
