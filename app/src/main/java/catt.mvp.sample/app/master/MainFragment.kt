package catt.mvp.sample.app.master

import catt.mvp.sample.R
import catt.mvp.sample.app.proxy.MainFragmentImpl
import catt.mvp.framework.app.BaseFragment
import catt.mvp.framework.proxy.ILifecycle

class MainFragment : BaseFragment() {
    override fun pageLabel(): String = "演示DialogFragment"

    override fun injectLayoutId(): Int = R.layout.fragment_main

    override fun injectProxyImpl(): ILifecycle<MainFragment> {
        return MainFragmentImpl()
    }
}