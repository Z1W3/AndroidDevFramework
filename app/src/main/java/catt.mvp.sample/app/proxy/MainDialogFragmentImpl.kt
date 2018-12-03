package catt.mvp.sample.app.proxy

import android.util.Log
import catt.mvp.sample.app.interfaces.IMainDialogFragmentIFS
import catt.mvp.sample.app.master.MainDialogFragment
import catt.mvp.sample.base.proxy.ProxyBaseDialogFragment
import catt.mvp.sample.presenter.MainDialogPresenter

class MainDialogFragmentImpl
    : ProxyBaseDialogFragment<MainDialogFragment, IMainDialogFragmentIFS.View, MainDialogPresenter>(),
    IMainDialogFragmentIFS.View {


    private val _TAG : String by lazy { MainFragmentImpl::class.java.simpleName }

    override fun onViewLoadCompleted() {

    }

    override fun onContent(content: String) {
        Log.e(_TAG, "content = $content")
    }
}