package com.vazh2100.geoeventapp

import android.app.Application
import com.vazh2100.core.coreAModule
import com.vazh2100.feature_events.featureEventsModule
import com.vazh2100.geolocation.geolocationStatusModule
import com.vazh2100.network.networkStatusModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

internal class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(
                listOf(
                    coreAModule,
                    networkStatusModule,
                    geolocationStatusModule,
                    featureEventsModule
                )
            )
        }
    }
}
