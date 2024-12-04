package com.vazh2100.core

import com.vazh2100.core.data.repository.LocationRepository
import com.vazh2100.core.data.repository.NetworkStateRepository
import com.vazh2100.core.domain.usecase.GetLocationStatusUseCase
import com.vazh2100.core.domain.usecase.GetNetworkStatusUseCase
import com.vazh2100.core.domain.usecase.IGetLocationStatusUseCase
import com.vazh2100.core.domain.usecase.IGetNetworkStatusUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Dependency injection module definition for the core module.
 * This module configures and provides instances of various components
 * used across the application such as APIs, repositories, use cases, and view models.
 */
val coreModule = module {

    // **API Layer**
    // Configures API, networking clients, and interceptors.
    // **Storage Layer**
    // Configures local database, DAO, and preferences storage.
    // **Repository Layer**
    // Provides implementations of repositories for accessing and managing data.
    single<LocationRepository> { LocationRepository(context = androidContext()) }
    single<NetworkStateRepository> { NetworkStateRepository(context = androidContext()) }
    // **Use Cases**
    // Defines use case classes for business logic.
    factory<IGetNetworkStatusUseCase> { GetNetworkStatusUseCase(get()) }
    factory<IGetLocationStatusUseCase> { GetLocationStatusUseCase(get()) }
    // **View Models**
    // Provides ViewModel instances for the app's presentation layer.
    // **Entity Layer**
    // Provides utility classes and entities used in the Domain Layer.
}
