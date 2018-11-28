package catt.mvp.sample.base.function.component

import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log.w
import android.widget.FrameLayout
import catt.mvp.sample.base.app.BaseFragment

interface ISupportFragmentComponent {
    /**
     * 通过ID获取当前FrameLayout中的Fragment
     * params id, This is FrameLayoutID
     */
    fun FragmentManager.findFragmentById(id: Int): Fragment? =
        if (id > 0) findFragmentById(id) else null

    /**
     * 通过FrameLayout获取Fragment
     * @link #findFragmentById(Int)
     */
    fun FragmentManager.findFragmentByFrameLayout(layout: FrameLayout): Fragment? = findFragmentById(layout.id)

    /**
     * 如果该Fragment未添加，则添加Fragment
     * 如果该Fragment已经被添加那么做替换处理
     *
     * @code FragmentTransaction.add(Int,Fragment)
     * @code FragmentTransaction.replace(Int,Fragment)
     */
    fun FragmentTransaction.commitFragment(id: Int, fragment: BaseFragment<*>, addToBackStack:Boolean = false, name: String? = null) {
        if (id <= 0) IllegalArgumentException("id cannot be less than or equal to 0.")
        synchronized(this) {
            if (fragment.isAdded) replace(id, fragment).addToBackStack(addToBackStack, name).compatCommit()
            else add(id, fragment).addToBackStack(addToBackStack, name).compatCommit(allowingStateLoss = false)
            return@synchronized
        }
    }

    /**
     * @link #commitFragment(Int, BaseFragment)
     */
    fun FragmentTransaction.commitFragment(layout: FrameLayout, fragment: BaseFragment<*>) {
        commitFragment(layout.id, fragment)
    }

    /**
     * 切换Fragment，但会保留Fragment的实例对象，前一个Fragment仅隐藏处理
     * 不进行实例操作，仅隐藏
     * PS: 如果Fragment未添加，不会主动进行添加
     */
    fun FragmentTransaction.commitSwitchFragment(from: BaseFragment<*>, to: BaseFragment<*>) {
        synchronized(this) {
            if (to.isAdded) hide(from).show(to).compatCommit()
            else w(FragmentTransaction::class.java.simpleName, "destination not added.")
            return@synchronized
        }
    }

    private fun FragmentTransaction.addToBackStack(addToBackStack:Boolean, name: String?) =
            when(addToBackStack){
                true -> addToBackStack(name)
                false -> this
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