package catt.mvp.sample.app.master

import catt.mvp.sample.app.proxy.MainActivityImpl
import catt.mvp.sample.R
import catt.mvp.sample.base.app.BaseActivity
import catt.mvp.sample.base.proxy.ILifecycle

class MainActivity : BaseActivity<MainActivity>(){

    private val _TAG : String by lazy { MainActivity::class.java.simpleName }

    override fun injectLayoutId(): Int = R.layout.activity_main

    override fun injectProxyImpl(): ILifecycle<MainActivity> {
        return MainActivityImpl(this@MainActivity)
    }
}
