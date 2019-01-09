package catt.mvp.sample.presenter

import catt.mvp.sample.app.interfaces.IMainFragment
import catt.mvp.sample.base.presenter.BasePresenter

class MainFragmentPresenter : BasePresenter(), IMainFragment.Presenter {

    init {
        Thread{
            Thread.sleep(1000L)
            setContent()
        }.start()
    }

    override fun setContent() {
        getViewInterface<IMainFragment.View>().onContent("This is MainFragmentPresenter")
    }

    override fun onDestroy() {

    }
}