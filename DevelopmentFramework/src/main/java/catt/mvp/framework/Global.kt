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

internal var isSPT:Boolean = false


internal lateinit var globalServiceBaseUrl:String
internal var globalGlideCacheMemory:Long = 0L
internal var globalGlideCachePath:String = ".OwnGlideCache"

/**
 * @param ctx:Context                       必填      用户上下文
 * @param property: String                  必填      适配像素比（example："1920x1080,2046x1536"）
 * @param serviceBaseUrl: String            必填      网络请求地址 host
 * @param umengAppId: String                必填      友盟APPID
 * @param umengChannel: String              必填      友盟渠道
 * @param umengSecretKey: String            选填      友盟安全KEY
 * @param connectTimeout: Long              选填      OkRetrofit框架-默认连接超时
 * @param readTimeout: Long                 选填      OkRetrofit框架-默认读取超时
 * @param headerInterceptor: Interceptor    选填      OkRetrofit框架-请求头部拦截器
 * @param loggingInterceptor: Interceptor   选填      OkRetrofit框架-网络访问日志拦截器
 * @param glideCacheMemory: Long            选填      glide缓存大小
 * @param glideCachePath: String            选填      glide缓存地址
 * @param toastColorNormal: Int             选填      toast颜色
 * @param toastColorInfo: Int               选填      toast颜色
 * @param toastColorWarning: Int            选填      toast颜色
 * @param toastColorSuccess: Int            选填      toast颜色
 * @param toastColorError: Int              选填      toast颜色
 * @param toastSize : Int                   选填      toast文字大小
 * @param isSimpleResponseToast:Boolean     选填      如果true,网络返回的错误信息均提示网络相关问题
 */
@ExperimentalCoroutinesApi
fun initializeDevelopmentFrameworks(
    ctx: Context,
    property: String,
    serviceBaseUrl: String,
    umengAppId: String,
    umengChannel: String,
    umengSecretKey: String = "",
    connectTimeout: Long = 6180L * 2,
    readTimeout: Long = 6180L,
    headerInterceptor: Interceptor? = null,
    loggingInterceptor: Interceptor? = null,
    glideCacheMemory: Long = 1024L * 1024L * 24L,
    glideCachePath: String = ".OwnGlideCache",
    @ColorInt
    toastColorNormal: Int = Color.parseColor("#FFFFFF"),
    @ColorInt
    toastColorInfo: Int = Color.parseColor("#2A7DCE"),
    @ColorInt
    toastColorWarning: Int = Color.parseColor("#E39224"),
    @ColorInt
    toastColorSuccess: Int = Color.parseColor("#2EBB7E"),
    @ColorInt
    toastColorError: Int = Color.parseColor("#F04848"),
    toastSize : Int = 14,
    isSimpleResponseToast:Boolean = false
) {
    globalServiceBaseUrl = serviceBaseUrl
    globalGlideCacheMemory = glideCacheMemory
    globalGlideCachePath = glideCachePath
    isSPT = isSimpleResponseToast
    globalContext = ctx.applicationContext
    /*TargetScreenMetrics 必须在主线程且必须第一个初始化,  否则 java.lang.ArithmeticException: divide by zero*/
    TargetScreenMetrics.get().initContent(ctx, property)
    GlobalScope.launch(Dispatchers.IO, CoroutineStart.ATOMIC) {
        UMConfigure.init(
            ctx,
            umengAppId, umengChannel,
            UMConfigure.DEVICE_TYPE_PHONE, umengSecretKey
        )
        MobclickAgent.setScenarioType(ctx, MobclickAgent.EScenarioType.E_UM_NORMAL)
        MobclickAgent.setCatchUncaughtExceptions(false)
        initializeNetwork(connectTimeout, readTimeout, headerInterceptor, loggingInterceptor)
        Toasty.Config.getInstance().generatedConfig(
            toastColorNormal, toastColorInfo, toastColorWarning, toastColorSuccess, toastColorError, toastSize).apply()
    }
}

private fun initializeNetwork(
    connectTimeout: Long,
    readTimeout: Long,
    headerInterceptor: Interceptor?,
    loggingInterceptor: Interceptor?
) =
    OkRetrofit.initializeNetwork(connectTimeout, readTimeout, headerInterceptor, loggingInterceptor)


private fun Toasty.Config.generatedConfig(
    @ColorInt n: Int, @ColorInt i: Int,
    @ColorInt w: Int, @ColorInt s: Int,
    @ColorInt e: Int, toastSize:Int
): Toasty.Config =
    setTextColor(n)
        .setInfoColor(i)
        .setWarningColor(w)
        .setSuccessColor(s)
        .setErrorColor(e)
        .tintIcon(true)
        .setTextSize(toastSize)