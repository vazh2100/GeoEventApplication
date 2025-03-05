package events

import core.entities.AssetReader
import events.data.api.AssetInterceptor
import events.data.api.EventsApi
import events.data.api.EventsClientProvider
import events.data.repository.EventRepository
import events.data.storage.EventsPreferencesStorage
import events.data.storage.room.DatabaseProvider
import events.data.storage.room.EventDao
import events.data.storage.room.EventsDatabase
import events.screen.event_list.EventListViewModel
import events.usecase.GetFilteredEventsUseCase
import events.usecase.GetSavedFiltersUseCase
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
    // API, networking clients, and interceptors.
    single<OkHttpClient> { EventsClientProvider.createHttpClient(get()) }
    single<Json> { EventsClientProvider.createJson() }
    single<Retrofit> { EventsClientProvider.createRetrofit(get(), get()) }
    single<EventsApi> { EventsClientProvider.createMainApi(get()) }
    single<AssetInterceptor> { AssetInterceptor(get()) }
    // Storages
    single<EventsDatabase> { DatabaseProvider.getDatabase(androidContext()) }
    single<EventDao> { get<EventsDatabase>().eventDao() }
    single<EventsPreferencesStorage> { EventsPreferencesStorage(androidContext()) }
    // Repositories
    single<EventRepository> { EventRepository(get(), get(), get()) }
    // Use cases
    factory { GetFilteredEventsUseCase(get(), get(), get(), get()) }
    factory { GetSavedFiltersUseCase(get()) }
    // View Models
    viewModel<EventListViewModel> { EventListViewModel(get(), get(), get(), get()) }
    // Entities
    single { AssetReader(androidContext()) }
}
