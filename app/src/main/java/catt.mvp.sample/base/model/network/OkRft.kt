package catt.mvp.sample.base.model.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object OkRft {

    @JvmStatic
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(6180L, TimeUnit.MILLISECONDS)
            .readTimeout(6180L, TimeUnit.MILLISECONDS)
            .addNetworkInterceptor(HeaderInterceptor())
            .addNetworkInterceptor(LoggingInterceptor())
            .build()


    }

    @JvmStatic
    private val apiRetrofit:Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://dev.zuul.hcb66.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @JvmStatic
    fun <T> create(service:Class<T>) : T = apiRetrofit.create(service)
}