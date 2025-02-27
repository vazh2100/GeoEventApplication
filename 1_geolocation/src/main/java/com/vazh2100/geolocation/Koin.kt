package com.vazh2100.geolocation

import com.vazh2100.geolocation.repository.LocationStateRepository
import com.vazh2100.geolocation.usecase.GetLocationStatusUseCase
import com.vazh2100.geolocation.usecase.IGetLocationStatusUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val geolocationStatusModule = module {
    // **Repository Layer**
    single<LocationStateRepository> {
        LocationStateRepository(
            context = androidContext()
        )
    }
    // **Use Cases**
    factory<IGetLocationStatusUseCase> { GetLocationStatusUseCase(get()) }
}
