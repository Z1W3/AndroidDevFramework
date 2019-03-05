package catt.mvp.sample.presenter

import catt.mvp.sample.app.interfaces.IMainFragment
import catt.mvp.framework.presenter.BasePresenter

class MainFragmentPresenter : BasePresenter(), IMainFragment.Presenter {
    override fun onCreate() {

    }

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