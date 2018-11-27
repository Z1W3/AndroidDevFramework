package catt.mvp.sample.presenter

import catt.mvp.sample.app.interfaces.IMainDialogFragmentIFS
import catt.mvp.sample.base.mvp.presenter.BasePresenter

class MainDialogPresenter : BasePresenter<IMainDialogFragmentIFS.View>(), IMainDialogFragmentIFS.Presenter {
    init {
        Thread{
            Thread.sleep(1000L)
            setContent()
        }.start()
    }

    override fun setContent() {
        viewIFS?.onContent("This is MainDialogPresenter")
    }

    override fun onDestroy() {

    }
}