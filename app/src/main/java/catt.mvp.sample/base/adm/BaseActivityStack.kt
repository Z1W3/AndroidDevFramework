package catt.mvp.sample.base.adm

import android.util.Log
import android.util.Log.e
import catt.mvp.sample.base.app.BaseActivity
import java.util.*

internal class BaseActivityStack : IStack<BaseActivity<*>> {
    private val stack: Stack<BaseActivity<*>> by lazy { Stack<BaseActivity<*>>() }
    override fun push(target: BaseActivity<*>) {
        synchronized(target) {
            stack.remove(target)
            Log.e("ActivityStack", "push name=${target::class.java.name}, isPaused=${target.isPaused}, hashCode=${target.hashCode()}")
            stack.push(target)
            return@synchronized
        }
    }

    override fun pop() {
        peek()?.apply {
            finish()
            stack.pop()
        }
    }

    override fun remove(target: BaseActivity<*>) {
        Log.e("ActivityStack", "remove name=${target::class.java.name}, isPaused=${target.isPaused}, hashCode=${target.hashCode()}")
        stack.remove(target)
    }

    override fun peek(): BaseActivity<*>? {
        if (empty()) return null
        return stack.peek()
    }

    override fun empty(): Boolean = stack.empty()

    override fun search(target: BaseActivity<*>): BaseActivity<*>? {
        val absoluteIndex = stack.search(target)
        return when (!stack.empty() && absoluteIndex != -1) {
            true -> stack[absoluteIndex - 1]
            false -> null
        }
    }

    fun <T> search(clazz:Class<T>): T? {
        for (index in stack.indices.reversed()){
        }

        for (index in stack.indices.reversed()) {
            e("ActivityStack","search name=${stack[index]::class.java.name}, isPaused=${stack[index].isPaused}, hashCode=${stack[index].hashCode()}")
            if(!stack[index].isPaused && stack[index]::class.java.name == clazz.name){
                return stack[index] as T
            }
        }
        return null
    }

    internal fun killMyPid() {
        for (index in stack.indices.reversed()) {
            val aty = stack[index]
            aty.finish()
        }
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(1)
    }

    companion object {
        private object Single {
            internal val INSTANCE: BaseActivityStack by lazy { BaseActivityStack() }
        }

        @JvmStatic
        fun get(): BaseActivityStack = Single.INSTANCE
    }
}