package catt.mvp.sample.app.master

import catt.mvp.sample.R
import catt.mvp.sample.app.proxy.MainDialogFragmentImpl
import catt.mvp.framework.app.BaseDialogFragment
import catt.mvp.framework.proxy.ILifecycle


class MainDialogFragment : BaseDialogFragment() {
    override fun isDisallowUseBackKey(): Boolean {
        return false
    }

    override fun pageLabel(): String = "演示DialogFragment"

    override fun injectLayoutId(): Int = R.layout.dialog_main

    override fun injectProxyImpl(): ILifecycle<MainDialogFragment> {
        return MainDialogFragmentImpl()
    }
}