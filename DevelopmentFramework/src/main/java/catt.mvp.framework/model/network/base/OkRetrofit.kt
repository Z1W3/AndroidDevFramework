package catt.mvp.framework.model.network.base

import catt.mvp.framework.globalServiceBaseUrl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object OkRetrofit {

    private var currentTimeout:Long = 6180L
    private var readTimeout:Long = 6180L
    private var headerInterceptor : Interceptor? = null
    private var loggingInterceptor : Interceptor? = null

    @JvmStatic
    internal fun initializeNetwork(currentTimeout:Long = 6180L, readTimeout:Long = 6180L, headerInterceptor : Interceptor? = null, loggingInterceptor : Interceptor? = null){
        this.currentTimeout = currentTimeout
        this.readTimeout = readTimeout
        this.headerInterceptor = headerInterceptor
        this.loggingInterceptor = loggingInterceptor
    }

    @JvmStatic
    private val okHttpClient:OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(currentTimeout, TimeUnit.MILLISECONDS)
        builder.readTimeout(readTimeout, TimeUnit.MILLISECONDS)
        headerInterceptor?.apply {
            builder.addInterceptor(this@apply)
        }
        loggingInterceptor?.apply {
            builder.addInterceptor(this@apply)
        }
        builder.build()
    }

    @JvmStatic
    private val apiRetrofit:Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(globalServiceBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @JvmStatic
    fun <T> create(service:Class<T>) : T = apiRetrofit.create(service)


    @JvmStatic
    val gson:Gson by lazy { GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create() }
}