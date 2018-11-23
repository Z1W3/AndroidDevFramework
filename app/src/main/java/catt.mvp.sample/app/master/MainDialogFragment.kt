package catt.mvp.sample.app.master

import catt.mvp.sample.R
import catt.mvp.sample.app.proxy.MainDialogFragmentImpl
import catt.mvp.sample.base.app.BaseDialogFragment
import catt.mvp.sample.base.proxy.ILifecycle


class MainDialogFragment : BaseDialogFragment<MainDialogFragment>() {
    override fun injectLayoutId(): Int = R.layout.dialog_main

    override fun injectProxyImpl(): ILifecycle<MainDialogFragment> {
        return MainDialogFragmentImpl(this@MainDialogFragment)
    }
}