package catt.mvp.sample.base.model.network

import android.util.Log.i
import okhttp3.Interceptor
import okhttp3.Response

class LoggingInterceptor : Interceptor {

    private val _TAG by lazy { "OkHttp.Interceptor" }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val t1 = System.nanoTime()
        i(_TAG, "Sending request ${request.url()} on ${chain.connection()}\n" +
                "Headers: ${request.headers()}\n" +
                "Method: ${request.method()}")
        val response = chain.proceed(request)
        val t2 = System.nanoTime()
        i(_TAG, "Received response for ${response.request().url()} in ${(t2 - t1)}\n" +
                "Code=${response.code()} Message=${response.message()}\n" +
                "Body=${response.body()?.string()}")
        return response
    }

}