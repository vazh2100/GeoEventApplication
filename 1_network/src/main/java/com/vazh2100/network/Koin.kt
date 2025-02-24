package com.vazh2100.network

import com.vazh2100.network.repository.NetworkStateRepository
import com.vazh2100.network.usecase.GetNetworkStatusUseCase
import com.vazh2100.network.usecase.IGetNetworkStatusUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkStatusModule = module {
    // **Repositories**
    single<NetworkStateRepository> { NetworkStateRepository(context = androidContext()) }
    // **Use Cases**
    factory<IGetNetworkStatusUseCase> { GetNetworkStatusUseCase(get()) }
}
