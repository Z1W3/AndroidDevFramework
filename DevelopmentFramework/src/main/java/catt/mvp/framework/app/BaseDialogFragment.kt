package catt.mvp.framework.app

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import catt.compat.layout.app.CompatLayoutDialogFragment
import catt.mvp.framework.adm.DialogFragmentStack
import catt.mvp.framework.proxy.IProxy
import catt.mvp.framework.proxy.ProxyBaseDialogFragment
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.*
import org.android.eventbus.EventBus

abstract class BaseDialogFragment : CompatLayoutDialogFragment(), IProxy, LifecycleOwner {

    private val lifecycleRegistry:LifecycleRegistry by lazy{ LifecycleRegistry(this@BaseDialogFragment) }

    var isPaused:Boolean = false

    var isShowing: Boolean = false

    /**
     * 友盟记录
     */
    abstract fun pageLabel():String

    abstract fun injectLayoutId(): Int

    private val widthLayoutSize: Int
        get() = proxy.widthLayoutSize!!


    private val heightLayoutSize: Int
        get() = proxy.heightLayoutSize


    override val proxy: ProxyBaseDialogFragment<*>by lazy {
        injectProxyImpl() as ProxyBaseDialogFragment<*>
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        lifecycleRegistry.addObserver(proxy)
    }

    override fun onStart() {
        super.onStart()
        dialog.window!!.setLayout(widthLayoutSize, heightLayoutSize)
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        hideSystemUI()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        compatCreateView(injectLayoutId(), container)?.postOnViewLoadCompleted()

    override fun onHiddenChanged(hidden: Boolean){
        proxy.onHiddenChanged(hidden)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        DialogFragmentStack.get().push(this@BaseDialogFragment)
        super.onViewCreated(view, savedInstanceState)
        proxy.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        proxy.onActivityCreated(savedInstanceState, arguments)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        proxy.onActivityResult(requestCode, resultCode, data)
    }


    override fun onDestroyView() {
        this.clearFindViewByIdCache()
        proxy.onDestroyView()
        super.onDestroyView()
        isShowing = false
        DialogFragmentStack.get().remove(this@BaseDialogFragment)
        System.runFinalization()
    }


    override fun onResume() {
        super.onResume()
        isPaused = false
        proxy.onResume()
        hideSystemUI()
        MobclickAgent.onPageStart(pageLabel())
    }

    override fun onPause() {
        super.onPause()
        isPaused = true
        proxy.onPause()
        MobclickAgent.onPageEnd(pageLabel())
    }

    override fun onStop() {
        super.onStop()
        proxy.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        lifecycleRegistry.removeObserver(proxy)

    }

    private fun View.postOnViewLoadCompleted():View {
        post {
            isShowing = true
            proxy.onViewLoadCompleted() }
        return this
    }

    fun hideSystemUI() {
        val decorView = dialog.window.decorView
        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        decorView.systemUiVisibility = flags

    }
}