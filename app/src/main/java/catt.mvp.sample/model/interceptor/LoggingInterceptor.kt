package catt.mvp.sample.model.interceptor

import android.util.Log.i
import catt.mvp.framework.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class LoggingInterceptor : Interceptor {

    private val _TAG by lazy { "OkHttp.Interceptor" }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val t1 = System.nanoTime()

         if(BuildConfig.DEBUG){
            i(_TAG, "Sending request ${request.url()}\n" +
                    "connection: ${chain.connection()}\n" +
                    "request method: ${request.method()}\n" +
                    "headers: {\n" +
                    "${request.headers()}}")
        }

        val response:Response = chain.proceed(request)

        if(BuildConfig.DEBUG){
            i(_TAG, "Received response for ${response.request().url()}\n" +
                    "time-consuming:${TimeUnit.MILLISECONDS.convert(System.nanoTime() - t1, TimeUnit.NANOSECONDS)}ms\n" +
                    "code=${response.code()}\n" +
                    "message=${response.message()}")
        }
        return response
    }
}