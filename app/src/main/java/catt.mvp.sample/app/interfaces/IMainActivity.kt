package catt.mvp.sample.app.interfaces

import catt.mvp.sample.base.proxy.annotations.DeclaredPresenterInterface
import catt.mvp.sample.base.proxy.annotations.DeclaredViewInterface


interface IMainActivity {

    @DeclaredViewInterface
    interface View {
        fun onContent(content: String)
    }

    @DeclaredPresenterInterface
    interface Presenter {
        fun setContent()
    }
}