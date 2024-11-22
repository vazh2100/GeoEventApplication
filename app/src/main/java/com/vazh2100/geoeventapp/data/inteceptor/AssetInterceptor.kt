package com.vazh2100.geoeventapp.data.inteceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

import java.io.IOException

class AssetInterceptor(private val context: Context, private val targetPath: String = "event") :
    Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url().toString()

        if (url.contains(targetPath)) {
            val jsonFile = "sample_data.json" // Путь к файлу в assets
            val assetContent = context.assets.open(jsonFile).bufferedReader().use {
                it.readText().trim()
            }

            return Response
                .Builder()
                .request(request)
                .protocol(okhttp3.Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(okhttp3.ResponseBody.create(null, assetContent))
                .addHeader("Content-Type", "application/json")
                .build()
        }

        return chain.proceed(request)
    }
}