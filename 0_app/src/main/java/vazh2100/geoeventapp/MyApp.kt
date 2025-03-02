package vazh2100.geoeventapp

import android.app.Application
import core.coreAModule
import events.featureEventsModule
import geolocation.geolocationStatusModule
import network.networkStatusModule
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
