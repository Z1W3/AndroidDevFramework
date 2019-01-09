package catt.mvp.sample.app.interfaces

import catt.mvp.sample.base.proxy.annotations.DeclaredPresenterInterface
import catt.mvp.sample.base.proxy.annotations.DeclaredViewInterface

interface IMainDialogFragment {

    @DeclaredViewInterface
    interface View {
        fun onContent(content: String)
    }

    @DeclaredPresenterInterface
    interface Presenter {
        fun setContent()
    }
}