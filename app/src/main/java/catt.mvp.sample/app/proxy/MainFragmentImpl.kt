package catt.mvp.sample.app.proxy

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log.e
import android.view.View
import android.widget.Button
import catt.mvp.sample.R
import catt.mvp.sample.app.interfaces.IMainFragment
import catt.mvp.sample.app.master.MainFragment
import catt.mvp.framework.function.component.newInstanceOrigin
import catt.mvp.framework.function.component.show
import catt.mvp.framework.proxy.ProxyBaseFragment
import catt.mvp.framework.proxy.annotations.InjectPresenter

@InjectPresenter("catt.mvp.sample.presenter.MainFragmentPresenter")
class MainFragmentImpl : ProxyBaseFragment<MainFragment>(), IMainFragment.View {
    override val tag: String get() = MainFragmentImpl::class.java.name

    private val _TAG: String by lazy { MainFragmentImpl::class.java.simpleName }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        e(_TAG, "onViewCreated:")
        view.findViewById<Button>(R.id.dialog_btn).setOnClickListener {
            target?.apply {
                MainDialogFragmentImpl::class.java.newInstanceOrigin<DialogFragment>().show(fragmentTransaction!!)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?, arguments: Bundle?) {
        super.onActivityCreated(savedInstanceState, arguments)
    }

    override fun onViewLoadCompleted() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onContent(content: String) {
        e(_TAG, "content = $content")
    }
}