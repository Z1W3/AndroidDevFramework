package catt.mvp.sample.app.master

import catt.mvp.sample.R
import catt.mvp.sample.app.proxy.MainFragmentImpl
import catt.mvp.sample.base.app.BaseFragment
import catt.mvp.sample.base.proxy.ILifecycle

class MainFragment : BaseFragment<MainFragment>() {

    override fun injectLayoutId(): Int = R.layout.fragment_main

    override fun injectProxyImpl(): ILifecycle<MainFragment> {
        return MainFragmentImpl(this@MainFragment)
    }
}