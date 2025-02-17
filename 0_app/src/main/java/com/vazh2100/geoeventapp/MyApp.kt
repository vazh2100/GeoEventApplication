package com.vazh2100.geoeventapp

import android.app.Application
import com.vazh2100.core.coreModule
import com.vazh2100.feature_events.featureEventsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

internal class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(
                listOf(
                    coreModule,
                    featureEventsModule
                )
            )
        }
    }
}
