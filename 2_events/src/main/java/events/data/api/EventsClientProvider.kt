package events.data.api

import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

internal object EventsClientProvider {

    fun createHttpClient(assetInterceptor: AssetInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(assetInterceptor).build()
    }

    fun createJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }

    fun createRetrofit(client: OkHttpClient, json: Json): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://baseUrl")
            .client(client)
            .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
            .build()
    }

    fun createMainApi(retrofit: Retrofit): EventsApi {
        return retrofit.create(EventsApi::class.java)
    }
}
