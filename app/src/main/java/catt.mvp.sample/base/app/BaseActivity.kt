package catt.mvp.sample.base.app

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
import catt.mvp.sample.base.mvp.view.IRootViewIFS
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.*


abstract class BaseActivity<T : CompatLayoutActivity> : CompatLayoutActivity(),
    IProxyLifecycle<T>,
    IRootViewIFS, PermissionHelper.OnPermissionListener {

    var isPaused:Boolean = false

    private val permission : IPermissionComponent by lazy { PermissionHelper(this, this) }

    abstract fun injectLayoutId():Int

    override val proxy: ProxyBaseActivity<T, IRootViewIFS, BasePresenter<IRootViewIFS>> by lazy {
        injectProxyImpl() as ProxyBaseActivity<T, IRootViewIFS, BasePresenter<IRootViewIFS>>
    }

    private val transaction by lazy { supportFragmentManager.beginTransaction() }

    override fun onCreate(savedInstanceState: Bundle?) {
        BaseActivityStack.get().push(this@BaseActivity)
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        proxy.onCreate(savedInstanceState)
        setContentView(injectLayoutId())
        permission.scan()
        window.decorView.postOnViewLoadCompleted()
    }

    override fun onGrantedPermissionCompleted() =
        proxy.onGrantedPermissionCompleted()

    override fun onResume() {
        super.onResume()
        isPaused = false
        proxy.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        isPaused = true
        proxy.onPause()
        MobclickAgent.onPause(this)
    }

    override fun onDestroy() {
        this.clearFindViewByIdCache()
        super.onDestroy()
        proxy.onDestroy()
        BaseActivityStack.get().remove(this@BaseActivity)
        System.runFinalization()
    }

    override fun onStart() {
        super.onStart()
        proxy.onStart()
    }

    override fun onRestart() {
        super.onRestart()
        proxy.onRestart()
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
