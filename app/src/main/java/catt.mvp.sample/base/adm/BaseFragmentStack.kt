package catt.mvp.sample.base.adm

import android.support.v4.app.Fragment
import android.util.Log
import catt.mvp.sample.base.app.BaseFragment
import java.util.*

internal class BaseFragmentStack : IStack<BaseFragment> {
    private val stack: Stack<BaseFragment> by lazy { Stack<BaseFragment>() }
    override fun push(target: BaseFragment) {
        synchronized(target) {
            stack.remove(target)
            Log.e("FragmentStack", "push name=${target::class.java.name}, isPaused=${target.isPaused}, hashCode=${target.hashCode()}")
            stack.push(target)
            return@synchronized
        }
    }

    override fun pop() {
        peek()?.apply {
            stack.pop()
        }
    }

    override fun remove(target: BaseFragment) {
        Log.e("FragmentStack", "remove name=${target::class.java.name}, isPaused=${target.isPaused}, hashCode=${target.hashCode()}")
        stack.remove(target)
    }

    override fun peek(): BaseFragment? {
        if (empty()) return null
        return stack.peek()
    }

    override fun empty(): Boolean = stack.empty()

    override fun search(target: BaseFragment): BaseFragment? {
        val absoluteIndex = stack.search(target)
        return when (!stack.empty() && absoluteIndex != -1) {
            true -> stack[absoluteIndex - 1]
            false -> null
        }
    }

    fun <T:Fragment> search(clazz:Class<T>): T? {
        for (index in stack.indices.reversed()) {
            Log.e("FragmentStack", "search name=${stack[index]::class.java.name}, isPaused=${stack[index].isPaused}, hashCode=${stack[index].hashCode()}")
            if(!stack[index].isPaused && stack[index]::class.java.name == clazz.name){
                return stack[index] as T
            }
        }
        return null
    }

    companion object {
        private object Single {
            internal val INSTANCE: BaseFragmentStack by lazy { BaseFragmentStack() }
        }

        @JvmStatic
        fun get(): BaseFragmentStack = Single.INSTANCE
    }
}