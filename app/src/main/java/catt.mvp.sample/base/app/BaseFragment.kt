package catt.mvp.sample.base.app

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleRegistry
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import catt.compat.layout.app.CompatLayoutFragment
import catt.mvp.sample.base.adm.BaseFragmentStack
import catt.mvp.sample.base.presenter.BasePresenter
import catt.mvp.sample.base.proxy.IProxy
import catt.mvp.sample.base.proxy.ProxyBaseFragment
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.*

abstract class BaseFragment<T : CompatLayoutFragment> : CompatLayoutFragment(),
    IProxy<T> {

    private val _TAG by lazy { BaseFragment::class.java.simpleName }
    private val lifecycleRegistry:LifecycleRegistry by lazy{ LifecycleRegistry(this@BaseFragment) }

    var isPaused:Boolean = false
    /**
     * 友盟记录
     */
    abstract fun pageLabel():String

    abstract fun injectLayoutId(): Int

    override val proxy: ProxyBaseFragment<T, *, BasePresenter<*>>by lazy {
        injectProxyImpl() as ProxyBaseFragment<T, *, BasePresenter<*>>
    }

    val fragmentTransaction: FragmentTransaction
        get() = childFragmentManager.beginTransaction()

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleRegistry.addObserver(proxy)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        compatCreateView(injectLayoutId(), container)?.postOnViewLoadCompleted()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        BaseFragmentStack.get().push(this)
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

    override fun onHiddenChanged(hidden: Boolean){
        proxy.onHiddenChanged(hidden)
    }

    override fun onDestroyView() {
        this.clearFindViewByIdCache()
        proxy.onDestroyView()
        super.onDestroyView()
        BaseFragmentStack.get().remove(this)
        System.runFinalization()
    }


    override fun onResume() {
        super.onResume()
        isPaused = false
        MobclickAgent.onPageStart(pageLabel())
        hideSystemUI()
    }

    override fun onPause() {
        super.onPause()
        isPaused = true
        MobclickAgent.onPageEnd(pageLabel())
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegistry.removeObserver(proxy)
    }

    private fun View.postOnViewLoadCompleted():View {
        post { proxy.onViewLoadCompleted() }
        return this
    }

    fun hideSystemUI() {
        activity?.apply {
            val decorView = window.decorView
            val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            decorView.systemUiVisibility = flags

        }
    }
}