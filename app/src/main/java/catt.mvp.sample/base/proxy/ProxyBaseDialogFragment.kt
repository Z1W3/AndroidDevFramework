package catt.mvp.sample.base.proxy

import android.support.v4.app.DialogFragment
import catt.mvp.sample.base.view.IRootViewIFS
import catt.mvp.sample.base.presenter.BasePresenter
import java.lang.ref.Reference

/**
 * type T, 绑定DialogFragment
 * type V, 绑定View,所有ViewInterface均需继承IRootViewInterface
 * type P, 绑定Presenter,并且Presenter绑定View
 *
 * params reference, 绑定DialogFragment的引用类型,建议采用WeakReference<T>
 */
abstract class ProxyBaseDialogFragment<T: DialogFragment, V : IRootViewIFS, P: BasePresenter<V>>
constructor(override val reference: Reference<T>) : ProxyBaseFragment<T, V, P>(reference)