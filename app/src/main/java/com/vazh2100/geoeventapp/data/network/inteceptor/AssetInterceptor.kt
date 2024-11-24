package com.vazh2100.geoeventapp.data.network.inteceptor

import android.content.Context
import com.vazh2100.geoeventapp.domain.entities.AssetReader
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

class AssetInterceptor(
    private val context: Context,
    private val assetReader: AssetReader,
    private val targetPath: String = "event"
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url().toString()
        if (url.contains(targetPath)) {
            val content = assetReader.readJsonFromAsset(context)

            return Response
                .Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(ResponseBody.create(null, content))
                .addHeader("Content-Type", "application/json")
                .build()
        }
        return chain.proceed(request)
    }
}