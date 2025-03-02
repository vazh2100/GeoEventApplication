package network

import network.usecase.IObserveNetworkStateUseCase
import network.usecase.ObserveNetworkStateUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkStatusModule = module {
    // **Use Cases**
    single<IObserveNetworkStateUseCase> { ObserveNetworkStateUseCase(context = androidContext()) }
}
