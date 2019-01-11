package catt.mvp.sample.app.interfaces

import catt.mvp.framework.proxy.annotations.DeclaredPresenterInterface
import catt.mvp.framework.proxy.annotations.DeclaredViewInterface

interface IMainFragment {

    @DeclaredViewInterface
    interface View {
        fun onContent(content: String)
    }

    @DeclaredPresenterInterface
    interface Presenter {
        fun setContent()
    }
}