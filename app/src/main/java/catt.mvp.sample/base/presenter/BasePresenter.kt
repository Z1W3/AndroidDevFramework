package catt.mvp.sample.base.presenter

import catt.mvp.sample.base.view.IRootViewIFS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BasePresenter<V : IRootViewIFS>: CoroutineScope {
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    var viewIFS: V? = null

    fun onAttach(obj: V) {
        viewIFS = obj
        job = Job()
    }

    fun onDetach() {
        viewIFS = null
        job.cancel()
        onDestroy()
    }

    /**
     * 调用此方法后，应该销毁所有操作
     */
    abstract fun onDestroy()
}