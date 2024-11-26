package com.vazh2100.geoeventapp.domain.di

import com.vazh2100.geoeventapp.data.network.api.MainApi
import com.vazh2100.geoeventapp.data.network.client.MainClientProvider
import com.vazh2100.geoeventapp.data.network.inteceptor.AssetInterceptor
import com.vazh2100.geoeventapp.data.repository.EventRepository
import com.vazh2100.geoeventapp.data.repository.LocationRepository
import com.vazh2100.geoeventapp.data.repository.NetworkStateRepository
import com.vazh2100.geoeventapp.data.storages.device.PreferencesStorage
import com.vazh2100.geoeventapp.data.storages.room.AppDataBase
import com.vazh2100.geoeventapp.data.storages.room.DatabaseProvider
import com.vazh2100.geoeventapp.data.storages.room.dao.EventDao
import com.vazh2100.geoeventapp.domain.entities.AssetReader
import com.vazh2100.geoeventapp.domain.entities.EventsProcessor
import com.vazh2100.geoeventapp.domain.usecase.GetFilteredEventsUseCase
import com.vazh2100.geoeventapp.domain.usecase.GetLocationStatusUseCase
import com.vazh2100.geoeventapp.domain.usecase.GetNetworkStatusUseCase
import com.vazh2100.geoeventapp.domain.usecase.GetSavedFiltersUseCase
import com.vazh2100.geoeventapp.presentaion.screen.eventList.EventListViewModel
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.*
import org.koin.dsl.module
import retrofit2.Retrofit


/**
 * Dependency injection module definition for the GeoEventApp.
 * This module configures and provides instances of various components
 * used across the application such as APIs, repositories, use cases, and view models.
 */

val appModule = module {
    // **API Layer**
    // Configures API, networking clients, and interceptors.
    single<OkHttpClient> { MainClientProvider.createHttpClient(assetInterceptor = get()) }
    single<Json> { MainClientProvider.createJson() }
    single<Retrofit> { MainClientProvider.createRetrofit(client = get(), json = get()) }
    single<MainApi> { MainClientProvider.createMainApi(get()) }
    single<AssetInterceptor> { AssetInterceptor(assetReader = get()) }

    // **Storage Layer**
    // Configures local database, DAO, and preferences storage.
    single<AppDataBase> { DatabaseProvider.getDatabase(androidContext()) }
    single<EventDao> { get<AppDataBase>().eventDao() }
    single<PreferencesStorage> { PreferencesStorage(androidContext()) }

    // **Repository Layer**
    // Provides implementations of repositories for accessing and managing data.
    single<EventRepository> {
        EventRepository(
            eventDao = get<EventDao>(), mainApi = get<MainApi>(), preferenceStorage = get()
        )
    }
    single<LocationRepository> { LocationRepository(context = androidContext()) }
    single<NetworkStateRepository> { NetworkStateRepository(context = androidContext()) }

    // **Use Cases**
    // Defines use case classes for business logic.
    factory {
        GetFilteredEventsUseCase(
            eventRepository = get(),
            preferencesStorage = get(),
            getNetworkStatusUseCase = get(),
            getLocationStatusUseCase = get(),
            eventsProcessor = EventsProcessor
        )
    }
    factory { GetSavedFiltersUseCase(preferencesStorage = get()) }
    factory { GetNetworkStatusUseCase(get()) }
    factory { GetLocationStatusUseCase(get()) }

    // **View Models**
    // Provides ViewModel instances for the app's presentation layer.
    viewModel<EventListViewModel> {
        EventListViewModel(
            getFilteredEventsUseCase = get(),
            getSavedFiltersUseCase = get(),
            getNetworkStatusUseCase = get(),
            getLocationStatusUseCase = get()
        )
    }

    // **Entity Layer**
    // Provides utility classes and entities used in the Domain Layer.
    single<AssetReader> { AssetReader(androidContext()) }
}

