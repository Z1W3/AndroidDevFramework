package catt.mvp.sample.base.presenter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.lang.ClassCastException
import kotlin.coroutines.CoroutineContext

abstract class BasePresenter: CoroutineScope{
    private val job: Job by lazy { Job() }

    override val coroutineContext: CoroutineContext by lazy { job + Dispatchers.Main }

    private lateinit var view:Any

    fun <T> getViewInterface(): T = view as T

    fun onAttach(viewClass: Class<out Any>, o: Any) {
        val cast = viewClass.cast(o)
        cast?:throw ClassCastException("View convert error.")
        this.view = cast
    }

    fun onDetach() {
        job.cancel()
        onDestroy()
    }

    /**
     * 调用此方法后，应该销毁所有操作
     */
    abstract fun onDestroy()
}