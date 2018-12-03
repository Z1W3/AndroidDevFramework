package catt.mvp.sample.base.adm

import android.support.v4.app.Fragment
import java.util.*

internal class BaseFragmentStack : IStack<Fragment> {
    private val stack: Stack<Fragment> by lazy { Stack<Fragment>() }
    override fun push(target: Fragment) {
        synchronized(target) {
            stack.remove(target)
            stack.push(target)
            return@synchronized
        }
    }

    override fun pop() {
        peek()?.apply {
            stack.pop()
        }
    }

    override fun remove(target: Fragment) {
        stack.remove(target)
    }

    override fun peek(): Fragment? {
        if (empty()) return null
        return stack.peek()
    }

    override fun empty(): Boolean = stack.empty()

    override fun search(target: Fragment): Fragment? {
        val absoluteIndex = stack.search(target)
        return when (!stack.empty() && absoluteIndex != -1) {
            true -> stack[absoluteIndex - 1]
            false -> null
        }
    }

    fun <T> search(clazz:Class<T>): T? {
        for (index in stack.indices.reversed()) {
            if(stack[index]::class.java.name == clazz.name){
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