package catt.mvp.sample

import android.app.Application
import android.content.Context
import android.graphics.Color
import catt.compat.layout.internal.TargetScreenMetrics
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.*


@ExperimentalCoroutinesApi
class GlobalApplication : Application() {
    private val _TAG by lazy { GlobalApplication::class.java.simpleName }
    override fun onCreate() {
        super.onCreate()
        initializeFrameworks(applicationContext)
    }

    private fun initializeFrameworks(ctx: Context) {
        /*TargetScreenMetrics 必须在主线程且必须第一个初始化,  否则 java.lang.ArithmeticException: divide by zero*/
        TargetScreenMetrics.get().initContent(ctx, "1920x1080,2046x1536")
        GlobalScope.async(Dispatchers.IO, CoroutineStart.ATOMIC) {
            UMConfigure.init(
                ctx,
                BuildConfig.UMENG_APP_IDENTITY, BuildConfig.UMENG_CHANNEL,
                UMConfigure.DEVICE_TYPE_PHONE, BuildConfig.UMENG_SECRET_KEY
            )
            MobclickAgent.setScenarioType(ctx, MobclickAgent.EScenarioType.E_UM_NORMAL)
            MobclickAgent.setCatchUncaughtExceptions(false)
            GlobalCrashHandler.get().initContext(ctx)
            Toasty.Config.getInstance().generatedConfig().apply()
            return@async
        }
    }

    private fun Toasty.Config.generatedConfig(): Toasty.Config =
        setErrorColor(Color.parseColor("#F04848"))
            .setInfoColor(Color.parseColor("#2A7DCE"))
            .setSuccessColor(Color.parseColor("#2EBB7E"))
            .setWarningColor(Color.parseColor("#E39224"))
            .setTextColor(Color.parseColor("#FFFFFF"))
            .tintIcon(true)
            .setTextSize(14)
}