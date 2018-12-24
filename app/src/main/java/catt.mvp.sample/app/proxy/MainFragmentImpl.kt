package catt.mvp.sample.app.proxy

import android.os.Bundle
import android.util.Log.e
import android.view.View
import android.widget.Button
import catt.mvp.sample.R
import catt.mvp.sample.app.interfaces.IMainFragmentInterIFS
import catt.mvp.sample.app.master.MainDialogFragment
import catt.mvp.sample.app.master.MainFragment
import catt.mvp.sample.base.proxy.ProxyBaseFragment
import catt.mvp.sample.presenter.MainFragmentPresenter

class MainFragmentImpl :
    ProxyBaseFragment<MainFragment, IMainFragmentInterIFS.View, MainFragmentPresenter>(),
    IMainFragmentInterIFS.View {

    private val _TAG: String by lazy { MainFragmentImpl::class.java.simpleName }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.dialog_btn).setOnClickListener {
            target?.apply {
                MainDialogFragment().show(childFragmentManager, MainDialogFragment::class.java.simpleName)
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