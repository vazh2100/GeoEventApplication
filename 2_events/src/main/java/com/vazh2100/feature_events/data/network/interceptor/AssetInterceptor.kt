package com.vazh2100.feature_events.data.network.interceptor

import com.vazh2100.feature_events.domain.entities.AssetReader
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

internal class AssetInterceptor(
    private val assetReader: AssetReader,
    private val targetPath: String = "event"
) : Interceptor {

    companion object {
        const val CODE_OK = 200
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url().toString()
        if (url.contains(targetPath)) {
            val content = assetReader.readJsonFromAsset()

            return Response
                .Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(CODE_OK)
                .message("OK")
                .body(ResponseBody.create(null, content))
                .addHeader("Content-Type", "application/json")
                .build()
        }
        return chain.proceed(request)
    }
}
