package catt.mvp.sample.base.app

import android.os.Bundle
import android.view.View
import catt.compat.layout.app.CompatLayoutActivity
import catt.mvp.sample.R
import catt.mvp.sample.base.proxy.IProxyLifecycle
import catt.mvp.sample.base.adm.BaseActivityStack
import catt.mvp.sample.base.mvp.presenter.BasePresenter
import catt.mvp.sample.base.proxy.ProxyBaseActivity
import catt.mvp.sample.base.mvp.view.IRootViewIFS
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.*


abstract class BaseActivity<T : CompatLayoutActivity> : CompatLayoutActivity(),
    IProxyLifecycle<T>,
    IRootViewIFS {

    abstract fun injectLayoutId():Int

    override val proxy: ProxyBaseActivity<T, IRootViewIFS, BasePresenter<IRootViewIFS>> by lazy {
        injectProxyImpl() as ProxyBaseActivity<T, IRootViewIFS, BasePresenter<IRootViewIFS>>
    }

    private val transaction by lazy { supportFragmentManager.beginTransaction() }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        BaseActivityStack.get().push(this@BaseActivity)
        super.onCreate(savedInstanceState)
        proxy.onCreate(savedInstanceState)
        setContentView(injectLayoutId())
        window.decorView.postOnViewLoadCompleted()
    }

    override fun onResume() {
        super.onResume()
        proxy.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        proxy.onPause()
        MobclickAgent.onPause(this)
    }

    override fun onDestroy() {
        this.clearFindViewByIdCache()
        super.onDestroy()
        proxy.onDestroy()
        BaseActivityStack.get().remove(this@BaseActivity)
    }

    override fun onStart() {
        super.onStart()
        proxy.onStart()
    }

    override fun onRestart() {
        super.onRestart()
        proxy.onRestart()
    }

    private fun View.postOnViewLoadCompleted() {
        post { proxy.onViewLoadCompleted() }
    }
}
