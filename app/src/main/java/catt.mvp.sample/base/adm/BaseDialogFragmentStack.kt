package catt.mvp.sample.base.adm

import android.support.v4.app.DialogFragment
import catt.mvp.sample.base.app.BaseDialogFragment
import java.util.*
import kotlin.collections.ArrayList

internal class BaseDialogFragmentStack : IStack<BaseDialogFragment<*>> {
    private val dialogStack: Stack<BaseDialogFragment<*>> by lazy { Stack<BaseDialogFragment<*>>() }
    override fun push(target: BaseDialogFragment<*>) {
        synchronized(target) {
            dialogStack.remove(target)
            dialogStack.push(target)
            return@synchronized
        }
    }

    override fun pop() {
        peek()?.apply {
            dismissAllowingStateLoss()
            dialogStack.pop()
        }
    }

    override fun remove(target: BaseDialogFragment<*>) {
        dialogStack.remove(target)
    }

    override fun peek(): BaseDialogFragment<*>? {
        if(empty()) return null
        return dialogStack.peek()
    }

    override fun empty(): Boolean = dialogStack.empty()

    override fun search(target: BaseDialogFragment<*>): BaseDialogFragment<*>? {
        val absoluteIndex = dialogStack.search(target)
        return when (!dialogStack.empty() && absoluteIndex != -1) {
            true -> dialogStack[absoluteIndex - 1]
            false -> null
        }
    }

    /**
     * 统计堆栈中的Dialog
     */
    internal fun statisticsStackDialog(): Array<DialogFragment>{
        if(empty()) return emptyArray()
        val list = ArrayList<DialogFragment>()
        for(index in dialogStack.indices.reversed()){
            list.add(dialogStack[index])
        }
        return list.toArray(arrayOf())
    }

    /**
     * 统计显示中的Dialog
     */
    internal fun statisticsShowingDialog(): Array<DialogFragment>{
        if(empty()) return emptyArray()
        val list = ArrayList<DialogFragment>()
        for(index in dialogStack.indices.reversed()){
            val dialog = dialogStack[index]
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
        private object ASM {
            internal val INSTANCE: BaseDialogFragmentStack by lazy { BaseDialogFragmentStack() }
        }

        @JvmStatic
        fun get(): BaseDialogFragmentStack = ASM.INSTANCE
    }
}