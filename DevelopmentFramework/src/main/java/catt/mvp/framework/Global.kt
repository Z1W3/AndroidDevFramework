package catt.mvp.framework

import android.content.Context
import android.graphics.Color
import android.support.annotation.ColorInt
import catt.compat.layout.internal.TargetScreenMetrics
import catt.mvp.framework.model.network.base.OkRetrofit
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.*
import okhttp3.Interceptor

lateinit var globalContext: Context

fun initializeNetwork(
    currentTimeout: Long = 6180L,
    readTimeout: Long = 6180L,
    headerInterceptor: Interceptor? = null,
    loggingInterceptor: Interceptor? = null
) =
    OkRetrofit.initializeNetwork(currentTimeout, readTimeout, headerInterceptor, loggingInterceptor)

fun initializeDevelopmentFrameworks(
    ctx: Context,
    property: String,
    @ColorInt
    normal: Int = Color.parseColor("#FFFFFF"),
    @ColorInt
    info: Int = Color.parseColor("#2A7DCE"),
    @ColorInt
    warning: Int = Color.parseColor("#E39224"),
    @ColorInt
    success: Int = Color.parseColor("#2EBB7E"),
    @ColorInt
    error: Int = Color.parseColor("#F04848")
) {
    globalContext = ctx.applicationContext
    /*TargetScreenMetrics 必须在主线程且必须第一个初始化,  否则 java.lang.ArithmeticException: divide by zero*/
    TargetScreenMetrics.get().initContent(ctx, property)
    GlobalScope.launch(Dispatchers.IO, CoroutineStart.ATOMIC) {
        UMConfigure.init(
            ctx,
            BuildConfig.UMENG_APP_IDENTITY, BuildConfig.UMENG_CHANNEL,
            UMConfigure.DEVICE_TYPE_PHONE, BuildConfig.UMENG_SECRET_KEY
        )
        MobclickAgent.setScenarioType(ctx, MobclickAgent.EScenarioType.E_UM_NORMAL)
        MobclickAgent.setCatchUncaughtExceptions(false)
        Toasty.Config.getInstance().generatedConfig(normal, info, warning, success, error).apply()
    }
}


private fun Toasty.Config.generatedConfig(
    @ColorInt normal: Int, @ColorInt info: Int,
    @ColorInt warning: Int, @ColorInt success: Int,
    @ColorInt erro: Int
): Toasty.Config =
    setTextColor(normal)
        .setInfoColor(info)
        .setWarningColor(warning)
        .setSuccessColor(success)
        .setErrorColor(erro)
        .tintIcon(true)
        .setTextSize(14)