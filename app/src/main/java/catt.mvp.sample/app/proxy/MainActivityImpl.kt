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

class MainActivityImpl :
    ProxyBaseActivity<MainActivity, IMainActivityIFS.View, MainActivityPresenter>(),
    IMainActivityIFS.View {

    private val _TAG: String by lazy { MainActivityImpl::class.java.simpleName }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        e(_TAG, "onCreate: ${R.id.container_layout}")
        target?.supportFragmentManager!!.beginTransaction().commitFragment(R.id.container_layout, MainFragment())
    }

    override fun onContent(content: String) {
        e(_TAG, "content = $content")
        val imageView: ImageView? = null
        context?.toastSuccess("aAAAA")
    }


    override fun onGrantedPermissionCompleted() {
        target?.toastSuccess("权限已全部授权")
    }


    override fun onViewLoadCompleted() {
        e(_TAG, "onViewLoadCompleted: ")
    }

}