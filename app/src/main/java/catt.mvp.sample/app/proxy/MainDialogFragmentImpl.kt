package catt.mvp.sample.app.proxy

import android.util.Log
import catt.mvp.sample.app.interfaces.IMainDialogFragmentIFS
import catt.mvp.sample.app.master.MainDialogFragment
import catt.mvp.sample.base.proxy.ProxyBaseDialogFragment
import catt.mvp.sample.presenter.MainDialogPresenter
import java.lang.ref.WeakReference

class MainDialogFragmentImpl(target: MainDialogFragment)
    : ProxyBaseDialogFragment<MainDialogFragment, IMainDialogFragmentIFS.View, MainDialogPresenter>(WeakReference(target)),
    IMainDialogFragmentIFS.View {


    private val _TAG : String by lazy { MainFragmentImpl::class.java.simpleName }

    override fun onViewLoadCompleted() {

    }

    override fun onContent(content: String) {
        Log.e(_TAG, "content = $content")
    }
}