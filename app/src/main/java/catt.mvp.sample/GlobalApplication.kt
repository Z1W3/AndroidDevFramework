package catt.mvp.sample

import android.app.Application
import android.content.Context
import catt.mvp.framework.initializeDevelopmentFrameworks
import catt.mvp.sample.model.interceptor.HeaderInterceptor
import catt.mvp.sample.model.interceptor.LoggingInterceptor
import kotlinx.coroutines.*


@ExperimentalCoroutinesApi
class GlobalApplication : Application() {
    private val _TAG by lazy { GlobalApplication::class.java.simpleName }
    override fun onCreate() {
        super.onCreate()
        initializeDevelopmentFrameworks(
            applicationContext,
            "1920x1080,2046x1536",
            connectTimeout = 10L * 2,
            readTimeout = 10L,
            headerInterceptor = HeaderInterceptor(),
            loggingInterceptor = LoggingInterceptor(),
            serviceBaseUrl =  BuildConfig.SERVICE_BASE_URL,
            umengAppId = BuildConfig.UMENG_APP_IDENTITY,
            umengChannel = BuildConfig.UMENG_CHANNEL,
            umengSecretKey = BuildConfig.UMENG_SECRET_KEY,
            glideCacheMemory = BuildConfig.GLIDE_CACHE_MEMORY,
            glideCachePath = BuildConfig.GLIDE_CACHE_PATH,
            toastSize = 40,
            isSimpleResponseToast = true
        )
        initialize(applicationContext)
    }

    private fun initialize(ctx: Context) {
        GlobalScope.async(Dispatchers.IO, CoroutineStart.ATOMIC) {
            GlobalCrashHandler.get().initContext(ctx)
        }
    }
}