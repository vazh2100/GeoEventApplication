package network

import network.repository.NetworkStateRepository
import network.usecase.IObserveNetworkStateUseCase
import network.usecase.ObserveNetworkStateUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkStatusModule = module {
    // **Repository**
    single<NetworkStateRepository> { NetworkStateRepository(context = androidContext()) }
    // **Use Cases**
    single<IObserveNetworkStateUseCase> { ObserveNetworkStateUseCase(networkStateRepository = get()) }
}
