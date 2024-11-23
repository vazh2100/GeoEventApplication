package com.vazh2100.geoeventapp.domain.di

import com.vazh2100.geoeventapp.data.api.MainApi
import com.vazh2100.geoeventapp.data.client.MainClientProvider
import com.vazh2100.geoeventapp.data.inteceptor.AssetInterceptor
import com.vazh2100.geoeventapp.data.repository.EventRepository
import com.vazh2100.geoeventapp.data.repository.LocationRepository
import com.vazh2100.geoeventapp.data.storages.room.AppDataBase
import com.vazh2100.geoeventapp.data.storages.room.DatabaseProvider
import com.vazh2100.geoeventapp.data.storages.room.dao.EventDao
import com.vazh2100.geoeventapp.domain.entities.AssetReader
import com.vazh2100.geoeventapp.domain.usecase.GetFilteredEventsUseCase
import com.vazh2100.geoeventapp.presentaion.screen.eventList.EventListViewModel
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    // Entity Layer
    // Предоставляет классы сущностей и утилиты, используемые в Domain Layer.
    single<AssetReader> { AssetReader() }

    // API Layer
    // Настройка API, клиентской части и сетевого взаимодействия.
    single<OkHttpClient> { MainClientProvider.createHttpClient(assetInterceptor = get()) }
    single<Json> { MainClientProvider.createJson() }
    single<Retrofit> { MainClientProvider.createRetrofit(client = get(), json = get()) }
    single<MainApi> { MainClientProvider.createMainApi(get()) }
    single<AssetInterceptor> { AssetInterceptor(androidContext(), assetReader = get()) }

    // Storage Layer
    // Настройка локальной базы данных и DAO.
    single<AppDataBase> { DatabaseProvider.getDatabase(androidContext()) }
    single<EventDao> { get<AppDataBase>().eventDao() }

    // Repository Layer
    // Предоставляет реализации репозиториев для доступа к данным.
    single<EventRepository> {
        EventRepository(
            eventDao = get<EventDao>(), mainApi = get<MainApi>()
        )
    }
    single<LocationRepository> { LocationRepository(context = androidContext()) }

    //Use Cases
    factory {
        GetFilteredEventsUseCase(eventRepository = get(), locationRepository = get())
    }

    //View Models
    viewModel<EventListViewModel> { EventListViewModel(getFilteredEventsUseCase = get()) }
}
