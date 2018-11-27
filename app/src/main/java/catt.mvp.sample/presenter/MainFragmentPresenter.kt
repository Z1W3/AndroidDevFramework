package catt.mvp.sample.presenter

import catt.mvp.sample.app.interfaces.IMainFragmentInterIFS
import catt.mvp.sample.base.mvp.presenter.BasePresenter

class MainFragmentPresenter : BasePresenter<IMainFragmentInterIFS.View>(), IMainFragmentInterIFS.Presenter {

    init {
        Thread{
            Thread.sleep(1000L)
            setContent()
        }.start()
    }

    override fun setContent() {
        viewIFS?.onContent("This is MainFragmentPresenter")
    }

    override fun onDestroy() {

    }
}