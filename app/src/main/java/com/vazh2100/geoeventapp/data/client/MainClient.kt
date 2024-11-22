package com.vazh2100.geoeventapp.data.client

import com.vazh2100.geoeventapp.data.api.MainApi
import com.vazh2100.geoeventapp.data.inteceptor.AssetInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory


fun createHttpClient(assetInterceptor: AssetInterceptor): OkHttpClient {
    return OkHttpClient.Builder().addInterceptor(assetInterceptor).build()
}

fun createJson(assetInterceptor: AssetInterceptor): Json {
    return Json {
        ignoreUnknownKeys = true
    }
}

fun createRetrofit(client: OkHttpClient, json: Json): Retrofit {
    return Retrofit.Builder().baseUrl("baseUrl") // Указываем базовый URL
        .client(client) // Подключаем OkHttpClient
        .addConverterFactory(json.asConverterFactory(MediaType.get("application/json"))).build()
}

fun createMainApi(retrofit: Retrofit): MainApi {
    return retrofit.create(MainApi::class.java)
}