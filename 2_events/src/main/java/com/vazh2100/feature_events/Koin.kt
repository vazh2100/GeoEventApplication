package com.vazh2100.feature_events

import com.vazh2100.feature_events.data.network.api.EventsApi
import com.vazh2100.feature_events.data.network.client.EventsClientProvider
import com.vazh2100.feature_events.data.network.interceptor.AssetInterceptor
import com.vazh2100.feature_events.data.repository.EventRepository
import com.vazh2100.feature_events.data.storages.device.EventsPreferencesStorage
import com.vazh2100.feature_events.data.storages.room.DatabaseProvider
import com.vazh2100.feature_events.data.storages.room.EventsDatabase
import com.vazh2100.feature_events.data.storages.room.dao.EventDao
import com.vazh2100.feature_events.domain.entities.AssetReader
import com.vazh2100.feature_events.domain.usecase.GetFilteredEventsUseCase
import com.vazh2100.feature_events.domain.usecase.GetSavedFiltersUseCase
import com.vazh2100.feature_events.presentaion.screen.event_list.EventListViewModel
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 * Dependency injection module definition for the feature_events.
 * This module configures and provides instances of various components
 * used across the application such as APIs, repositories, use cases, and view models.
 */
val featureEventsModule = module {
    // **API Layer**
    // Configures API, networking clients, and interceptors.
    single<OkHttpClient> { EventsClientProvider.createHttpClient(assetInterceptor = get()) }
    single<Json> { EventsClientProvider.createJson() }
    single<Retrofit> { EventsClientProvider.createRetrofit(client = get(), json = get()) }
    single<EventsApi> { EventsClientProvider.createMainApi(get()) }
    single<AssetInterceptor> { AssetInterceptor(assetReader = get()) }
    // **Storage Layer**
    // Configures local database, DAO, and preferences storage.
    single<EventsDatabase> { DatabaseProvider.getDatabase(androidContext()) }
    single<EventDao> { get<EventsDatabase>().eventDao() }
    single<EventsPreferencesStorage> { EventsPreferencesStorage(androidContext()) }
    // **Repository Layer**
    // Provides implementations of repositories for accessing and managing data.
    single<EventRepository> {
        EventRepository(
            eventDao = get<EventDao>(),
            eventsApi = get<EventsApi>(),
            preferenceStorage = get()
        )
    }
    // **Use Cases**
    // Defines use case classes for business logic.
    factory {
        GetFilteredEventsUseCase(
            eventRepository = get(),
            eventsPreferencesStorage = get(),
            getNetworkStatus = get(),
            getLocationStatus = get(),
        )
    }
    factory { GetSavedFiltersUseCase(eventsPreferencesStorage = get()) }
    // **View Models**
    // Provides ViewModel instances for the app's presentation layer.
    viewModel<EventListViewModel> {
        EventListViewModel(
            getFilteredEvents = get(),
            getSavedFilters = get(),
            getNetworkStatus = get(),
            getLocationStatus = get()
        )
    }
    // **Entity Layer**
    // Provides utility classes and entities used in the Domain Layer.
    single {
        AssetReader(androidContext())
    }
}
