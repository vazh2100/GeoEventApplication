package geolocation

import geolocation.repository.LocationStateRepository
import geolocation.usecase.GetLocationStatusUseCase
import geolocation.usecase.IGetLocationStatusUseCase
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
