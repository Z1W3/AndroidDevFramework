package catt.mvp.sample.base.app

import android.os.Build
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.widget.FrameLayout
import catt.compat.layout.app.CompatLayoutActivity
import catt.compat.layout.app.CompatLayoutDialogFragment
import catt.compat.layout.app.CompatLayoutFragment
import catt.mvp.sample.base.proxy.IProxyLifecycle
import catt.mvp.sample.base.adm.BaseActivityStack
import catt.mvp.sample.base.adm.BaseDialogFragmentStack
import catt.mvp.sample.base.presenter.BasePresenter
import catt.mvp.sample.base.proxy.ProxyBaseActivity
import catt.mvp.sample.base.view.IRootViewIFS
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
        BaseActivityStack.get().push(this@BaseActivity)
        super.onCreate(savedInstanceState)
        proxy.onCreate(savedInstanceState)
        setContentView(injectLayoutId())
        window.decorView.apply {
            post { proxy.onViewLoadCompleted() }
        }
    }

    override fun onResume() {
        super.onResume()
        proxy.onResume()
    }

    override fun onPause() {
        super.onPause()
        proxy.onPause()
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

    /**
     * 销毁dialog
     * return, true 销毁成功, false销毁不成功，堆栈中没有该记录
     */
    internal fun <V : CompatLayoutDialogFragment> dismissTargetDialog(target: BaseDialogFragment<V>): Boolean =
        BaseDialogFragmentStack.get().dismissTarget(target)

    /**
     * 统计存活的Dialog
     */
    internal fun getLifeDialogArray(): Array<DialogFragment> {
        return BaseDialogFragmentStack.get().statisticsStackDialog()
    }

    /**
     * 统计显示中的Dialog
     */
    internal fun getShowingDialogArray(): Array<DialogFragment> {
        return BaseDialogFragmentStack.get().statisticsShowingDialog()
    }

    /**
     * 通过ID获取Fragment
     * params id, This is FrameLayoutID
     */
    internal fun findFragmentById(id: Int): Fragment? {
        return if (id > 0) supportFragmentManager.findFragmentById(id) else null
    }

    /**
     * @link #findFragmentById(Int)
     */
    internal fun findFragmentByFrameLayout(layout: FrameLayout): Fragment? = findFragmentById(layout.id)

    /**
     * 添加Fragment,如果该Fragment已经被添加那么做替换处理
     *
     * @code FragmentTransaction.add(Int,Fragment)
     * @code FragmentTransaction.replace(Int,Fragment)
     */
    internal fun <V : CompatLayoutFragment> addFragment(id: Int, fragment: BaseFragment<V>) {
        if (id <= 0) IllegalArgumentException("id cannot be less than or equal to 0.")
        synchronized(this) {
            if (fragment.isAdded) transaction.replace(id, fragment).compatCommit(allowingStateLoss = false)
            else transaction.add(id, fragment).compatCommit(allowingStateLoss = false)
            return@synchronized
        }
    }

    /**
     * @link #addFragment(Int, BaseFragment)
     */
    internal fun <V : CompatLayoutFragment> addFragment(layout: FrameLayout, fragment: BaseFragment<V>) {
        addFragment(layout.id, fragment)
    }

    /**
     * 切换Fragment，但会保留Fragment的实例对象，前一个Fragment仅隐藏处理
     */
    internal fun <V : CompatLayoutFragment> switchFragment(from: BaseFragment<V>, to: BaseFragment<V>) {
        synchronized(this) {
            if (to.isAdded) transaction.hide(from).show(to).compatCommit()
            else transaction.hide(from).add(from.id, to).compatCommit(allowingStateLoss = false)
            return@synchronized
        }
    }

    /**
     * 外部扩展兼容FragmentTransaction.commit()
     */
    private fun FragmentTransaction.compatCommit(allowingStateLoss: Boolean = true) {
        when (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            true -> if (allowingStateLoss) commitNowAllowingStateLoss() else commitNow()
            false -> if (allowingStateLoss) commitAllowingStateLoss() else commit()
        }
    }
}
