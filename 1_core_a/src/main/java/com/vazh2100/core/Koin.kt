package com.vazh2100.core

import com.vazh2100.core.data.repository.LocationRepository
import com.vazh2100.core.domain.usecase.GetLocationStatusUseCase
import com.vazh2100.core.domain.usecase.IGetLocationStatusUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Dependency injection module definition for the core module.
 * This module configures and provides instances of various components
 * used across the application such as APIs, repositories, use cases, and view models.
 */
val coreModule = module {
    // **Repository Layer** // Provides implementations of repositories for accessing and managing data.
    single<LocationRepository> { LocationRepository(context = androidContext()) }
    // **Use Cases** // Defines use case classes for business logic.
    factory<IGetLocationStatusUseCase> { GetLocationStatusUseCase(get()) }
}
