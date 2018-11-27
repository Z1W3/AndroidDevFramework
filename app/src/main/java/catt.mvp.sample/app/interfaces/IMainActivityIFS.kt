package catt.mvp.sample.app.interfaces

import catt.mvp.sample.base.mvp.view.IRootPresenterIFS
import catt.mvp.sample.base.mvp.view.IRootViewIFS

interface IMainActivityIFS {
    interface View : IRootViewIFS {
        fun onContent(content: String)
    }

    interface Presenter : IRootPresenterIFS {
        fun setContent()
    }
}