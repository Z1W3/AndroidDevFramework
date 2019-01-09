package catt.mvp.sample.base.proxy

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.view.View
import catt.mvp.sample.base.adm.BaseFragmentStack
import catt.mvp.sample.base.function.component.*
import catt.mvp.sample.base.presenter.BasePresenter
import catt.mvp.sample.base.proxy.annotations.DeclaredViewInterface
import catt.mvp.sample.base.proxy.annotations.InjectPresenter
import catt.mvp.sample.base.proxy.throwables.ProxyArgumentException
import java.lang.ref.Reference
import java.lang.ref.WeakReference
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 泛型注释
 * type T, 需要代理实现的Fragment
 * type V, View Interface
 * type P, Presenter类,
 *
 * 功能：
 * 获取代理Fragment类的对象
 * 对代理Fragment类进行弱引用处理
 * 获取Presenter类的对象
 */
abstract class ProxyBaseFragment<T : Fragment>
    : ILifecycle<T>, IDialogComponent {

    private val injectPresenter: InjectPresenter by lazy {
        val annotation = this@ProxyBaseFragment::class.java.getAnnotation(InjectPresenter::class.java)
        annotation?: throw ProxyArgumentException("Must be declaration annotation class InjectPresenter")
        annotation
    }

    val presenter by lazy { Class.forName(injectPresenter.value).newInstance() as BasePresenter }

    private val viewClass: Class<out Any> by lazy {
        this@ProxyBaseFragment::class.java.interfaces.forEach {
            it.getAnnotation(DeclaredViewInterface::class.java)?.apply {
                return@lazy this@ProxyBaseFragment::class.java.asSubclass(it)
            }
        }
        throw ProxyArgumentException("Must be declaration annotation class DeclaredViewInterface")
    }

    abstract val tag: String

    private var lifecycleState: Lifecycle.State = Lifecycle.State.INITIALIZED

    override val currentLifecycleState: Lifecycle.State
        get() = lifecycleState

    private val declaredClazz: Array<Type>
        get() {
            val genType = javaClass.genericSuperclass
            return (genType as ParameterizedType).actualTypeArguments
        }

    private val fragment: T?
        get() = BaseFragmentStack.get().search(declaredClazz[0] as Class<T>)

    override val reference: Reference<T>? by lazy {
        WeakReference<T>(fragment)
    }

    override val target: T?
        get() = reference?.get()

    val activity: FragmentActivity?
        get() = target?.activity

    override val context: Context?
        get() = reference?.get()?.context

    internal val fragmentTransaction: FragmentTransaction?
        get() = target?.childFragmentManager?.beginTransaction()

    override fun onCreate() {
    }

    open fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.onAttach(viewClass, this@ProxyBaseFragment)
    }

    open fun onActivityCreated(savedInstanceState: Bundle?, arguments: Bundle?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    open fun onDestroyView() {
        presenter.onDetach()
    }

    open fun onHiddenChanged(hidden: Boolean) {}

    override fun onStart() {}

    override fun onResume() {}

    override fun onPause() {}

    override fun onStop() {}

    override fun onAny(owner: LifecycleOwner) {
        lifecycleState = owner.lifecycle.currentState
    }

    override fun onDestroy() {}
}