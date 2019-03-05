package catt.mvp.sample

import android.app.Application
import android.content.Context
import catt.mvp.framework.initializeDevelopmentFrameworks
import catt.mvp.framework.initializeNetwork
import catt.mvp.sample.model.interceptor.HeaderInterceptor
import catt.mvp.sample.model.interceptor.LoggingInterceptor
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit


@ExperimentalCoroutinesApi
class GlobalApplication : Application() {
    private val _TAG by lazy { GlobalApplication::class.java.simpleName }
    override fun onCreate() {
        super.onCreate()
        initializeDevelopmentFrameworks(applicationContext, "1920x1080,2046x1536")
        initialize(applicationContext)
    }

    private fun initialize(ctx: Context) {
        GlobalScope.async(Dispatchers.IO, CoroutineStart.ATOMIC) {
            GlobalCrashHandler.get().initContext(ctx)
            initializeNetwork(
                currentTimeout = 10,
                readTimeout = 10,
                headerInterceptor = HeaderInterceptor(),
                loggingInterceptor = LoggingInterceptor()
            )
            return@async
        }
    }
}