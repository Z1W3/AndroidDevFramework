package catt.mvp.sample.presenter

import catt.mvp.sample.app.interfaces.IMainActivityIFS
import catt.mvp.sample.base.presenter.BasePresenter

class MainActivityPresenter : BasePresenter<IMainActivityIFS.View>(), IMainActivityIFS.Presenter {

    init {
        Thread{
            Thread.sleep(1000L)
            setContent()
        }.start()
    }

    override fun setContent() {
        viewIFS?.onContent("This is MainActivityPresenter")
    }

    override fun onDestroy() {

    }

}