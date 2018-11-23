package catt.mvp.sample.base.presenter

import catt.mvp.sample.base.view.IRootViewIFS
import java.util.*

abstract class BasePresenter<V : IRootViewIFS>: Observable() {

    var viewIFS: V? = null

    fun onAttach(obj: V) {
        viewIFS = obj
    }

    fun onDetach() {
        viewIFS = null
        onDestroy()
    }

    /**
     * 调用此方法后，应该销毁所有操作
     */
    abstract fun onDestroy()
}