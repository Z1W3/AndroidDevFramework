package catt.mvp.sample.base.mvp.model.network

import catt.mvp.sample.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object OkRft {

    @JvmStatic
    private val okHttpClient:OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(6180L, TimeUnit.MILLISECONDS)
            .readTimeout(6180L, TimeUnit.MILLISECONDS)
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(LoggingInterceptor())
            .build()
    }

    @JvmStatic
    private val apiRetrofit:Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @JvmStatic
    fun <T> create(service:Class<T>) : T = apiRetrofit.create(service)
}