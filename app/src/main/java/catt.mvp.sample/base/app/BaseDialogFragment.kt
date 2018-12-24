package catt.mvp.sample.base.app

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleRegistry
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import catt.compat.layout.app.CompatLayoutDialogFragment
import catt.mvp.sample.base.proxy.IProxy
import catt.mvp.sample.base.adm.BaseDialogFragmentStack
import catt.mvp.sample.base.mvp.presenter.BasePresenter
import catt.mvp.sample.base.proxy.ProxyBaseDialogFragment
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.*

abstract class BaseDialogFragment<T : CompatLayoutDialogFragment> : CompatLayoutDialogFragment(),
    IProxy<T> {

    private val lifecycleRegistry:LifecycleRegistry by lazy{ LifecycleRegistry(this@BaseDialogFragment) }

    var isPaused:Boolean = false

    var isShowing: Boolean = false

    /**
     * 友盟记录
     */
    abstract fun pageLabel():String

    abstract fun injectLayoutId(): Int

    override val proxy: ProxyBaseDialogFragment<T, *, BasePresenter<*>> by lazy {
        injectProxyImpl() as ProxyBaseDialogFragment<T, *, BasePresenter<*>>
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleRegistry.addObserver(proxy)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        compatCreateView(injectLayoutId(), container)?.postOnViewLoadCompleted()

    override fun onHiddenChanged(hidden: Boolean){
        proxy.onHiddenChanged(hidden)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        BaseDialogFragmentStack.get().push(this@BaseDialogFragment)
        super.onViewCreated(view, savedInstanceState)
        proxy.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        proxy.onActivityCreated(savedInstanceState, arguments)
    }

    override fun onDestroyView() {
        this.clearFindViewByIdCache()
        proxy.onDestroyView()
        super.onDestroyView()
        isShowing = false
        BaseDialogFragmentStack.get().remove(this@BaseDialogFragment)
        System.runFinalization()
    }


    override fun onResume() {
        super.onResume()
        isPaused = false
        MobclickAgent.onPageStart(pageLabel())
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
        post {
            isShowing = true
            proxy.onViewLoadCompleted() }
        return this
    }
}