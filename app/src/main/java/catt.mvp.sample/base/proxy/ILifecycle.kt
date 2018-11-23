package catt.mvp.sample.base.proxy

import android.content.Context
import android.os.Bundle
import java.lang.ref.Reference


interface ILifecycle<T>{
    val reference: Reference<T>

    val target: T?

    val context: Context?

    fun onCreate(savedInstanceState: Bundle?)

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onDestroy()

    /**
     * 系统已经完成加载layout布局文件
     */
    fun onViewLoadCompleted()
}