package catt.mvp.sample.base.adm

import android.support.v4.app.DialogFragment
import catt.mvp.sample.base.app.BaseDialogFragment
import java.util.*
import kotlin.collections.ArrayList

internal class BaseDialogFragmentStack : IStack<BaseDialogFragment<*>> {
    private val stack: Stack<BaseDialogFragment<*>> by lazy { Stack<BaseDialogFragment<*>>() }
    override fun push(target: BaseDialogFragment<*>) {
        synchronized(target) {
            stack.remove(target)
            stack.push(target)
            return@synchronized
        }
    }

    override fun pop() {
        peek()?.apply {
            dismissAllowingStateLoss()
            stack.pop()
        }
    }

    override fun remove(target: BaseDialogFragment<*>) {
        stack.remove(target)
    }

    override fun peek(): BaseDialogFragment<*>? {
        if(empty()) return null
        return stack.peek()
    }

    override fun empty(): Boolean = stack.empty()

    override fun search(target: BaseDialogFragment<*>): BaseDialogFragment<*>? {
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

    /**
     * 统计堆栈中的Dialog
     * array[0] 即 当前堆栈顶端Dialog
     */
    internal fun statisticsStackDialog(): Array<DialogFragment>{
        if(empty()) return emptyArray()
        val list = ArrayList<DialogFragment>()
        for(index in stack.indices.reversed()){
            list.add(stack[index])
        }
        return list.toArray(arrayOf())
    }

    /**
     * 统计显示中的Dialog
     * array[0] 即 当前堆栈顶端Dialog
     */
    internal fun statisticsShowingDialog(): Array<DialogFragment>{
        if(empty()) return emptyArray()
        val list = ArrayList<DialogFragment>()
        for(index in stack.indices.reversed()){
            val dialog = stack[index]
            if(dialog.isShowing) list.add(dialog)
        }
        return list.toArray(arrayOf())
    }

    internal fun dismissTarget(target: BaseDialogFragment<*>):Boolean {
        search(target)?.apply {
            dismissAllowingStateLoss()
            remove(target)
            return true
        }
        return false
    }

    companion object {
        private object Single {
            internal val INSTANCE: BaseDialogFragmentStack by lazy { BaseDialogFragmentStack() }
        }

        @JvmStatic
        fun get(): BaseDialogFragmentStack = Single.INSTANCE
    }
}