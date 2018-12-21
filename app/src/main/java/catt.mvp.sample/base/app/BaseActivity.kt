package catt.mvp.sample.base.app

import android.arch.lifecycle.Lifecycle
import android.content.Intent
import android.os.Bundle
import android.view.View
import catt.compat.layout.app.CompatLayoutActivity
import catt.mvp.sample.R
import catt.mvp.sample.base.proxy.IProxyLifecycle
import catt.mvp.sample.base.adm.BaseActivityStack
import catt.mvp.sample.base.function.component.IPermissionComponent
import catt.mvp.sample.base.function.helper.PermissionHelper
import catt.mvp.sample.base.mvp.presenter.BasePresenter
import catt.mvp.sample.base.proxy.ProxyBaseActivity
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.*
import android.arch.lifecycle.LifecycleRegistry


abstract class BaseActivity<T : CompatLayoutActivity> : CompatLayoutActivity(),
    IProxyLifecycle<T>, PermissionHelper.OnPermissionListener {

    private val lifecycleRegistry:LifecycleRegistry by lazy{LifecycleRegistry(this@BaseActivity)}

    var isPaused:Boolean = false

    private val permission : IPermissionComponent by lazy { PermissionHelper(this, this) }

    abstract fun injectLayoutId():Int

    override val proxy: ProxyBaseActivity<T, *, BasePresenter<*>> by lazy {
        injectProxyImpl() as ProxyBaseActivity<T, *, BasePresenter<*>>
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        BaseActivityStack.get().push(this@BaseActivity)
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        lifecycleRegistry.addObserver(proxy)
        setContentView(injectLayoutId())
        permission.scan()
        window.decorView.postOnViewLoadCompleted()
    }

    override fun onGrantedPermissionCompleted() =
        proxy.onGrantedPermissionCompleted()

    override fun onRestart() {
        super.onRestart()
        proxy.onRestart()
    }

    override fun onResume() {
        super.onResume()
        isPaused = false
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        isPaused = true
        MobclickAgent.onPause(this)
    }

    override fun onDestroy() {
        this.clearFindViewByIdCache()
        super.onDestroy()
        BaseActivityStack.get().remove(this@BaseActivity)
        lifecycleRegistry.removeObserver(proxy)
        System.runFinalization()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        permission.onActivityResultForPermissions(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permission.onPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun View.postOnViewLoadCompleted() {
        post { proxy.onViewLoadCompleted() }
    }
}
