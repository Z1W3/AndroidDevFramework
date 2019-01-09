package catt.mvp.sample.presenter

import catt.mvp.sample.app.interfaces.IMainDialogFragmentIFS
import catt.mvp.sample.base.presenter.BasePresenter

class MainDialogPresenter : BasePresenter(), IMainDialogFragmentIFS.Presenter {
    init {
        Thread{
            Thread.sleep(1000L)
            setContent()
        }.start()
    }

    override fun setContent() {
        getViewInterface<IMainDialogFragmentIFS.View>().onContent("This is MainDialogPresenter")
    }

    override fun onDestroy() {

    }
}