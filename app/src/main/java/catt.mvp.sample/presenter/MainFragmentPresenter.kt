package catt.mvp.sample.presenter

import catt.mvp.sample.app.interfaces.IMainFragmentInterIFS
import catt.mvp.sample.base.presenter.BasePresenter2

class MainFragmentPresenter : BasePresenter2(), IMainFragmentInterIFS.Presenter {

    init {
        Thread{
            Thread.sleep(1000L)
            setContent()
        }.start()
    }

    override fun setContent() {
        getViewInterface<IMainFragmentInterIFS.View>().onContent("This is MainFragmentPresenter")
    }

    override fun onDestroy() {

    }
}