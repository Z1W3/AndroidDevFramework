package catt.mvp.framework.function.component

import android.support.v4.app.DialogFragment
import catt.mvp.framework.adm.DialogFragmentStack
import catt.mvp.framework.app.BaseDialogFragment

interface IDialogComponent {

    fun peekCurrentDialog():BaseDialogFragment? =
        DialogFragmentStack.get().peek()

    fun popDismissDialog()=
        DialogFragmentStack.get().pop()

    /**
     * 销毁dialog
     * return, true 销毁成功, false销毁不成功，堆栈中没有该记录
     */
    fun dismissTargetDialog(target: BaseDialogFragment): Boolean =
        DialogFragmentStack.get().dismissTarget(target)

    /**
     * 统计存活的Dialog
     */
    fun getLifeDialogArray(): Array<DialogFragment> =
        DialogFragmentStack.get().statisticsStackDialog()

    /**
     * 统计显示中的Dialog
     */
    fun getShowingDialogArray(): Array<DialogFragment> =
        DialogFragmentStack.get().statisticsShowingDialog()
}