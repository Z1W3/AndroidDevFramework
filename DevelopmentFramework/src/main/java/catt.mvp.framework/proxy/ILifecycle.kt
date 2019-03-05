package catt.mvp.framework.proxy

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.Intent
import java.lang.ref.Reference


interface ILifecycle<T> : LifecycleObserver {

    val reference: Reference<T>?

    val currentLifecycleState: Lifecycle.State

    val target: T?

    val context: Context?

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume()

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause()

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy()

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAny(owner: LifecycleOwner)

    /**
     * 系统已经完成加载layout布局文件
     */
    fun onViewLoadCompleted()
}