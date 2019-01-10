package catt.mvp.sample.presenter

import catt.mvp.sample.app.interfaces.IMainDialogFragment
import catt.mvp.sample.base.presenter.BasePresenter

class MainDialogPresenter : BasePresenter(), IMainDialogFragment.Presenter {
    override fun onCreate() {

    }

    init {
        Thread{
            Thread.sleep(1000L)
            setContent()
        }.start()
    }

    override fun setContent() {
        getViewInterface<IMainDialogFragment.View>().onContent("This is MainDialogPresenter")
    }

    override fun onDestroy() {

    }
}