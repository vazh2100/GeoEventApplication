package com.vazh2100.network

import com.vazh2100.network.usecase.IObserveNetworkStateUseCase
import com.vazh2100.network.usecase.ObserveNetworkStateUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkStatusModule = module {
    // **Use Cases**
    single<IObserveNetworkStateUseCase> { ObserveNetworkStateUseCase(context = androidContext()) }
}
