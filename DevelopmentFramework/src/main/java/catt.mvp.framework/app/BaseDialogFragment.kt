package catt.mvp.framework.app

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import catt.compat.layout.app.CompatLayoutDialogFragment
import catt.mvp.framework.adm.BaseDialogFragmentStack
import catt.mvp.framework.proxy.IProxy
import catt.mvp.framework.proxy.ProxyBaseDialogFragment
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.*

abstract class BaseDialogFragment : CompatLayoutDialogFragment(), IProxy, LifecycleOwner {
    private val lifecycleRegistry:LifecycleRegistry by lazy{ LifecycleRegistry(this@BaseDialogFragment) }

    open val isEnableFullScreen :Boolean
        get() {
            val enableFullScreen = (activity as? BaseActivity)?.isEnableFullScreen
            enableFullScreen ?: return false
            return enableFullScreen
        }

    var isPaused:Boolean = false

    var isShowing: Boolean = false

    /**
     * 友盟记录
     */
    abstract fun pageLabel():String

    abstract fun injectLayoutId(): Int

    private val widthLayoutSize: Int
        get() = proxy.widthLayoutSize


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
        lifecycleRegistry.addObserver(proxy)
    }

    override fun onStart() {
        super.onStart()
        dialog.window!!.setLayout(widthLayoutSize, heightLayoutSize)
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setOnKeyListener(object : DialogInterface.OnKeyListener {
            override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
                return if ( keyCode == KeyEvent.KEYCODE_BACK && event!!.action == KeyEvent.ACTION_DOWN) {
                    isDisallowUseBackKey()
                } else {
                    false
                }
            }
        })
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        proxy.onActivityResult(requestCode, resultCode, data)
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
        proxy.onResume()
        if(isEnableFullScreen){
            dialog?.window?.setFullScreen()
        }
        MobclickAgent.onPageStart(pageLabel())
    }

    override fun onPause() {
        super.onPause()
        proxy.onPause()
        isPaused = true
        MobclickAgent.onPageEnd(pageLabel())
    }

    override fun onStop() {
        super.onStop()
        proxy.onStop()
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

    abstract fun isDisallowUseBackKey():Boolean
}

