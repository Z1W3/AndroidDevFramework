package catt.mvp.framework.app

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import catt.compat.layout.app.CompatLayoutFragment
import catt.mvp.framework.adm.BaseFragmentStack
import catt.mvp.framework.proxy.IProxy
import catt.mvp.framework.proxy.ProxyBaseFragment
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.*

abstract class BaseFragment : CompatLayoutFragment(), IProxy, LifecycleOwner {

    private val _TAG by lazy { BaseFragment::class.java.simpleName }
    private val lifecycleRegistry:LifecycleRegistry by lazy{ LifecycleRegistry(this@BaseFragment) }

    open val isEnableFullScreen :Boolean
        get() {
            val enableFullScreen = (activity as? BaseActivity)?.isEnableFullScreen
            enableFullScreen ?: return false
            return enableFullScreen
        }

    var isPaused:Boolean = false
    /**
     * 友盟记录
     */
    abstract fun pageLabel():String

    abstract fun injectLayoutId(): Int

    override val proxy: ProxyBaseFragment<*>by lazy {
        injectProxyImpl() as ProxyBaseFragment<*>
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
        BaseFragmentStack.get().push(this@BaseFragment)
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
        proxy.onResume()
        MobclickAgent.onPageStart(pageLabel())
        if(isEnableFullScreen){
            activity?.window?.setFullScreen()
        }
    }

    override fun onStop() {
        super.onStop()
        proxy.onStop()
    }

    override fun onPause() {
        super.onPause()
        proxy.onPause()
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
}