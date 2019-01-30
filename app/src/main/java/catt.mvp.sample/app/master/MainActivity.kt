package catt.mvp.sample.app.master

import catt.mvp.sample.app.proxy.MainActivityImpl
import catt.mvp.sample.R
import catt.mvp.framework.adm.ActivityStack
import catt.mvp.framework.app.BaseActivity
import catt.mvp.framework.proxy.ILifecycle

class MainActivity : BaseActivity() {

    override val injectStyleTheme: Int
        get() = R.style.AppTheme

    private val _TAG : String by lazy { MainActivity::class.java.simpleName }

    override fun injectLayoutId(): Int = R.layout.activity_main

    override fun injectProxyImpl(): ILifecycle<MainActivity> {
        return MainActivityImpl()
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityStack.get().killMyPid()
    }
}
