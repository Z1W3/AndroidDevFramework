package catt.mvp.sample

import android.app.Application
import android.graphics.Color
import catt.compat.layout.internal.TargetScreenMetrics
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.android.eventbus.EventBus


class GlobalApplication : Application() {
    private val _TAG by lazy { GlobalApplication::class.java.simpleName }
    override fun onCreate() {
        super.onCreate()
        initializeFrameworks()
    }

    private fun initializeFrameworks() {
        /*必须在主线程且必须第一个初始化*/TargetScreenMetrics.get().initContent(applicationContext, "2046x1536")
        GlobalScope.launch(Dispatchers.IO, CoroutineStart.ATOMIC) {
            UMConfigure.init(applicationContext, BuildConfig.UMENG_APP_IDENTITY, BuildConfig.UMENG_CHANNEL, UMConfigure.DEVICE_TYPE_PHONE, BuildConfig.UMENG_SECRET_KEY)
            MobclickAgent.setScenarioType(applicationContext, MobclickAgent.EScenarioType.E_UM_NORMAL)
            MobclickAgent.setCatchUncaughtExceptions(false)
            GlobalCrashHandler.get().initContext(applicationContext)
            Toasty.Config.getInstance().generatedConfig().apply()
            return@launch
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