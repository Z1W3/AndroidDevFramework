package catt.mvp.sample.base.adm

import android.app.Activity
import java.util.*

internal class BaseActivityStack : IStack<Activity> {
    private val atyStack: Stack<Activity> by lazy { Stack<Activity>() }
    override fun push(target: Activity) {
        synchronized(target) {
            atyStack.remove(target)
            atyStack.push(target)
            return@synchronized
        }
    }

    override fun pop() {
        peek()?.apply {
            finish()
            atyStack.pop()
        }
    }

    override fun remove(target: Activity) {
        atyStack.remove(target)
    }

    override fun peek(): Activity? {
        if (empty()) return null
        return atyStack.peek()
    }

    override fun empty(): Boolean = atyStack.empty()

    override fun search(target: Activity): Activity? {
        val absoluteIndex = atyStack.search(target)
        return when (!atyStack.empty() && absoluteIndex != -1) {
            true -> atyStack[absoluteIndex - 1]
            false -> null
        }
    }

    internal fun killMyPid() {
        for (index in atyStack.indices.reversed()) {
            val aty = atyStack[index]
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