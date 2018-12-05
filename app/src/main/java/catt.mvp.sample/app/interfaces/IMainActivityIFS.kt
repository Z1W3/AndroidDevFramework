package catt.mvp.sample.app.interfaces


interface IMainActivityIFS {
    interface View {
        fun onContent(content: String)
    }

    interface Presenter {
        fun setContent()
    }
}