package catt.mvp.framework.adm

import android.support.v4.app.DialogFragment
import android.util.Log
import catt.mvp.framework.app.BaseDialogFragment
import java.util.*
import kotlin.collections.ArrayList

class DialogFragmentStack : IStack<BaseDialogFragment> {
    private val stack: Stack<BaseDialogFragment> by lazy { Stack<BaseDialogFragment>() }

    @Synchronized
    override fun push(target: BaseDialogFragment) {
        synchronized(target) {
            stack.remove(target)
            Log.e("DialogFragmentStack", "push name=${target::class.java.name}, isPaused=${target.isPaused}, hashCode=${target.hashCode()}")
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

    override fun remove(target: BaseDialogFragment) {
        Log.e("DialogFragmentStack", "remove name=${target::class.java.name}, isPaused=${target.isPaused}, hashCode=${target.hashCode()}")
        stack.remove(target)
    }

    override fun peek(): BaseDialogFragment? {
        if(empty()) return null
        return stack.peek()
    }

    override fun empty(): Boolean = stack.empty()

    override fun search(target: BaseDialogFragment): BaseDialogFragment? {
        val absoluteIndex = stack.search(target)
        return when (!stack.empty() && absoluteIndex != -1) {
            true -> stack[absoluteIndex - 1]
            false -> null
        }
    }

    fun <T:DialogFragment> search(clazz:Class<T>): T? {
        for (index in stack.indices.reversed()) {
            Log.e("DialogFragmentStack", "search name=${stack[index]::class.java.name}, isPaused=${stack[index].isPaused}, hashCode=${stack[index].hashCode()}")
            if(!stack[index].isPaused && stack[index]::class.java.name == clazz.name){
                return stack[index] as T
            }
        }
        return null
    }

    /**
     * 统计堆栈中的Dialog
     * array[0] 即 当前堆栈顶端Dialog
     */
    fun statisticsStackDialog(): Array<DialogFragment>{
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
    fun statisticsShowingDialog(): Array<DialogFragment>{
        if(empty()) return emptyArray()
        val list = ArrayList<DialogFragment>()
        for(index in stack.indices.reversed()){
            val dialog = stack[index]
            if(dialog.isShowing) list.add(dialog)
        }
        return list.toArray(arrayOf())
    }

    fun dismissTarget(target: BaseDialogFragment):Boolean {
        search(target)?.apply {
            dismissAllowingStateLoss()
            remove(target)
            return true
        }
        return false
    }

    companion object {
        private object Single {
            val INSTANCE: DialogFragmentStack by lazy { DialogFragmentStack() }
        }

        @JvmStatic
        fun get(): DialogFragmentStack = Single.INSTANCE
    }
}