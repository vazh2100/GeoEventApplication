package com.vazh2100.geoeventapp.data.client

import com.vazh2100.geoeventapp.data.api.MainApi
import com.vazh2100.geoeventapp.data.inteceptor.AssetInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

fun createMainRetrofit(assetInterceptor: AssetInterceptor): MainApi {
    // Настраиваем OkHttpClient
    val client = OkHttpClient
        .Builder().addInterceptor(assetInterceptor).build()

    // Создаем JSON-сериализатор
    val json = Json {
        ignoreUnknownKeys = true // Игнорируем неизвестные ключи в JSON
    }

    // Создаем Retrofit
    val retrofit = Retrofit.Builder().baseUrl("baseUrl") // Указываем базовый URL
        .client(client) // Подключаем OkHttpClient
        .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
        .build()

    // Создаем реализацию интерфейса EventService
    return retrofit.create(MainApi::class.java)
}