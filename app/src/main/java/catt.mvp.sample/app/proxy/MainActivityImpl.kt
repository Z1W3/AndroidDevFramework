package catt.mvp.sample.app.proxy

import android.os.Bundle
import android.util.Log.e
import android.widget.ImageView
import catt.mvp.sample.R
import catt.mvp.sample.app.interfaces.IMainActivityIFS
import catt.mvp.sample.app.master.MainActivity
import catt.mvp.sample.app.master.MainFragment
import catt.mvp.sample.base.proxy.ProxyBaseActivity
import catt.mvp.sample.presenter.MainActivityPresenter
import java.lang.ref.WeakReference

class MainActivityImpl(target: MainActivity) :
    ProxyBaseActivity<MainActivity, IMainActivityIFS.View, MainActivityPresenter>(WeakReference(target)),
    IMainActivityIFS.View {

    private val _TAG: String by lazy { MainActivityImpl::class.java.simpleName }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        e(_TAG, "onCreate: ${R.id.container_layout}")
        target?.supportFragmentManager!!.beginTransaction()
            .replace(R.id.container_layout, MainFragment(), "MainFragment").commitAllowingStateLoss()

    }

    override fun onViewLoadCompleted() {
        e(_TAG, "onViewLoadCompleted: ")
    }

    override fun onContent(content: String) {
        e(_TAG, "content = $content")
        val imageView: ImageView? = null
    }
}