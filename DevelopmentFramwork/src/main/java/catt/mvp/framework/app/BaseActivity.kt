package catt.mvp.framework.app

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.content.Intent
import android.os.Bundle
import android.view.View
import catt.compat.layout.app.CompatLayoutActivity
import catt.mvp.framework.function.component.IPermissionComponent
import catt.mvp.framework.function.helper.PermissionHelper
import com.umeng.analytics.MobclickAgent
import android.arch.lifecycle.LifecycleRegistry
import android.support.v4.app.DialogFragment
import catt.mvp.framework.adm.BaseActivityStack
import catt.mvp.framework.adm.BaseDialogFragmentStack
import catt.mvp.framework.proxy.IProxy
import catt.mvp.framework.proxy.ProxyBaseActivity
import kotlinx.android.synthetic.*


abstract class BaseActivity : CompatLayoutActivity(), IProxy, PermissionHelper.OnPermissionListener, LifecycleOwner {

    private val lifecycleRegistry:LifecycleRegistry by lazy{LifecycleRegistry(this@BaseActivity)}

    var isPaused:Boolean = false

    private val permission : IPermissionComponent by lazy { PermissionHelper(this, this) }

    abstract fun injectLayoutId():Int

    override val proxy:ProxyBaseActivity<*> by lazy {
        injectProxyImpl() as ProxyBaseActivity<*>
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    abstract val injectStyleTheme:Int

    override fun onCreate(savedInstanceState: Bundle?) {
        BaseActivityStack.get().push(this@BaseActivity)
//        hideSystemUI()
        setTheme(injectStyleTheme)
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
        hideSystemUI()
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
        proxy.onActivityResult(requestCode, resultCode, data)
        permission.onActivityResultForPermissions(requestCode, resultCode, data)

        when(isContainDialogFragment()){
            true->{
                for (fragment in supportFragmentManager.fragments) {
                    fragment.onActivityResult(requestCode, resultCode, data)
                }
            }
            false->{
                for (fragment in supportFragmentManager.fragments) {
                    fragment.onActivityResult(requestCode, resultCode, data)
                }
                val statisticsShowingArray = BaseDialogFragmentStack.get().statisticsShowingDialog()
                for(index:Int in statisticsShowingArray.indices){
                    statisticsShowingArray[index].onActivityResult(requestCode, resultCode, data)
                }
            }
        }
    }

    private fun isContainDialogFragment():Boolean{
        for(fragment in supportFragmentManager.fragments){
            if(fragment is DialogFragment){
                return true
            }
        }
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permission.onPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun View.postOnViewLoadCompleted() {
        post { proxy.onViewLoadCompleted() }
    }

    fun hideSystemUI() {
        val decorView = window.decorView
        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        decorView.systemUiVisibility = flags
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus){
            hideSystemUI()
        }
    }
}
