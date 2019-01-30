package catt.mvp.framework.model.network.base

import catt.mvp.framework.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object OkRetrofit {

    private var currentTimeout:Long = 10
    private var readTimeout:Long = 10
    private var timeUnit:TimeUnit = TimeUnit.SECONDS

    private var headerInterceptor : Interceptor? = null
    private var loggingInterceptor : Interceptor? = null

    @JvmStatic
    internal fun initializeNetwork(
        currentTimeout:Long = 10,
        readTimeout:Long = 10,
        timeUnit:TimeUnit = TimeUnit.SECONDS,
        headerInterceptor : Interceptor? = null,
        loggingInterceptor : Interceptor? = null){
        this.currentTimeout = currentTimeout
        this.readTimeout = readTimeout
        this.timeUnit = timeUnit
        this.headerInterceptor = headerInterceptor
        this.loggingInterceptor = loggingInterceptor
    }

    @JvmStatic
    private val okHttpClient:OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(currentTimeout, timeUnit)
        builder.readTimeout(readTimeout, timeUnit)
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
            .baseUrl(BuildConfig.SERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @JvmStatic
    fun <T> create(service:Class<T>) : T = apiRetrofit.create(service)


    @JvmStatic
    val gson:Gson by lazy { GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create() }
}