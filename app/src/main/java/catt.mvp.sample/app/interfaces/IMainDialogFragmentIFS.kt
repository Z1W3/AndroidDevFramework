package catt.mvp.sample.app.interfaces

interface IMainDialogFragmentIFS {
    interface View {
        fun onContent(content: String)
    }

    interface Presenter {
        fun setContent()
    }
}