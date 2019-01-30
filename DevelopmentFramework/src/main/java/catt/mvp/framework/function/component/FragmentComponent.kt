package catt.mvp.framework.function.component

import android.os.Build
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.text.TextUtils
import android.util.Log
import android.widget.FrameLayout

/**
 * 通过ID获取当前FrameLayout中的Fragment
 * params position, This is FrameLayoutID
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
@Synchronized
fun FragmentTransaction.commitFragment(
    id: Int,
    fragment: Fragment,
    name: String = fragment::class.java.name,
    addToBackStack: Boolean = false
) {
    if (id <= 0) IllegalArgumentException("position cannot be less than or equal to 0.")
    synchronized(this) {
        if (fragment.isAdded) remove(fragment).replace(id, fragment).addToBackStack(addToBackStack, name).compatCommit()
        else replace(id, fragment).addToBackStack(addToBackStack, name).compatCommit(allowingStateLoss = false)
        return@synchronized
    }
}

/**
 * 如果该Fragment未添加，则添加Fragment
 *
 * @code FragmentTransaction.add(Int,Fragment)
 */
@Synchronized
fun FragmentTransaction.commitFragment(
    fragment: Fragment,
    name: String = fragment::class.java.name,
    addToBackStack: Boolean = false
) {
    synchronized(this) {
        if (fragment.isAdded) {
            remove(fragment)
        }
        add(fragment, name).addToBackStack(addToBackStack, name).compatCommit()
        return@synchronized
    }
}

/**
 * @link #commitFragment(Int, BaseFragment)
 */
fun FragmentTransaction.commitFragment(layout: FrameLayout, fragment: Fragment) {
    commitFragment(layout.id, fragment)
}

/**
 * 切换Fragment，但会保留Fragment的实例对象，前一个Fragment仅隐藏处理
 * 不进行实例操作，仅隐藏
 * PS: 如果Fragment未添加，不会主动进行添加
 */
@Synchronized
fun FragmentTransaction.commitSwitchFragment(from: Fragment, to: Fragment) {
    synchronized(this) {
        if (to.isAdded) hide(from).show(to).compatCommit()
        else Log.w(FragmentTransaction::class.java.simpleName, "destination not added.")
        return@synchronized
    }
}

private fun FragmentTransaction.addToBackStack(addToBackStack: Boolean, name: String?) =
    when (addToBackStack) {
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

fun DialogFragment.show(ft: FragmentTransaction, tag: String = "") {
    if (isAdded) ft.remove(this)
    ft.add(
        this, when (TextUtils.isEmpty(tag)) {
            true -> "${this.hashCode()}"
            false -> tag
        }
    ).compatCommit()
}