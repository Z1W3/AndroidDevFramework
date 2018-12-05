package catt.mvp.sample.app.interfaces

interface IMainFragmentInterIFS {
    interface View {
        fun onContent(content: String)
    }

    interface Presenter {
        fun setContent()
    }
}