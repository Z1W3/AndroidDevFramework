package catt.mvp.sample

import android.app.Application
import catt.compat.layout.internal.TargetScreenMetrics

class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TargetScreenMetrics.get().initContent(applicationContext, "2046x1536")
    }
}