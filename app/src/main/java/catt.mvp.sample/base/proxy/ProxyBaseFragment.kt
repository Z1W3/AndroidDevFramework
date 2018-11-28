package catt.mvp.sample.base.proxy

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import catt.mvp.sample.base.function.component.IDialogComponent
import catt.mvp.sample.base.function.component.IGlideComponent
import catt.mvp.sample.base.function.component.ISupportFragmentComponent
import catt.mvp.sample.base.function.component.IToastyComponent
import catt.mvp.sample.base.mvp.view.IRootViewIFS
import catt.mvp.sample.base.mvp.presenter.BasePresenter
import java.lang.ref.Reference
import java.lang.reflect.Constructor
import java.lang.reflect.ParameterizedType

/**
 * type T, 绑定Fragment
 * type V, 绑定View,所有ViewInterface均需继承IRootViewInterface
 * type P, 绑定Presenter,并且Presenter绑定View
 *
 * params reference, 绑定Fragment的引用类型,建议采用WeakReference<T>
 */
abstract class ProxyBaseFragment<T: Fragment, V : IRootViewIFS, P: BasePresenter<V>>
constructor(override val reference: Reference<T>) : ILifecycle<T>,
    IGlideComponent, IToastyComponent, ISupportFragmentComponent, IDialogComponent {

    override val target: T?
        get() = reference.get()

    private val presenterClazz: Class<P>
        get() {
            val genType = javaClass.genericSuperclass
            val params = (genType as ParameterizedType).actualTypeArguments.reversed()
            return params[0] as Class<P>
        }

    val presenter: P by lazy {
        val c: Constructor<P> = presenterClazz.getConstructor()
        c.newInstance()
    }

    val activity:FragmentActivity?
        get() = target?.activity

    override val context: Context?
        get() = reference.get()?.context

    override fun onCreate(savedInstanceState: Bundle?) {
    }

    open fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.onAttach(this as V)
    }

    open fun onActivityCreated(savedInstanceState: Bundle?) {

    }

    open fun onDestroyView() {
        presenter.onDetach()
    }

    open fun onHiddenChanged(hidden: Boolean){}

    override fun onStart() {}

    override fun onResume() {}

    override fun onPause() {}

    override fun onStop() {}

    override fun onDestroy() {}
}