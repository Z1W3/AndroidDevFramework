package catt.mvp.sample.base.proxy

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.view.View
import catt.mvp.sample.base.adm.BaseDialogFragmentStack
import catt.mvp.sample.base.adm.BaseFragmentStack
import catt.mvp.sample.base.function.component.IDialogComponent
import catt.mvp.sample.base.function.component.IGlideComponent
import catt.mvp.sample.base.function.component.ISupportFragmentComponent
import catt.mvp.sample.base.function.component.IToastyComponent
import catt.mvp.sample.base.mvp.view.IRootViewIFS
import catt.mvp.sample.base.mvp.presenter.BasePresenter
import java.lang.ref.Reference
import java.lang.ref.WeakReference
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * type T, 绑定DialogFragment
 * type V, 绑定View,所有ViewInterface均需继承IRootViewInterface
 * type P, 绑定Presenter,并且Presenter绑定View
 *
 * params reference, 绑定DialogFragment的引用类型,建议采用WeakReference<T>
 */
abstract class ProxyBaseDialogFragment<T: DialogFragment, V : IRootViewIFS, P: BasePresenter<V>>
    : ILifecycle<T>, IGlideComponent, IToastyComponent, ISupportFragmentComponent, IDialogComponent {

    private val declaredClazz: Array<Type>
        get() {
            val genType = javaClass.genericSuperclass
            return (genType as ParameterizedType).actualTypeArguments
        }

    private val dialog : T?
        get() = BaseDialogFragmentStack.get().search(declaredClazz[0] as Class<T>)

    val presenter: P by lazy { (declaredClazz[declaredClazz.size - 1] as Class<P>).getConstructor().newInstance() }

    override val reference: Reference<T>? by lazy {
        WeakReference<T>(dialog)
    }

    override val target: T?
        get() = reference?.get()

    val activity: FragmentActivity?
        get() = target?.activity

    override val context: Context?
        get() = reference?.get()?.context

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