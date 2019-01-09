package catt.mvp.sample.app.proxy

import android.support.v4.app.Fragment
import android.util.Log.e
import android.widget.ImageView
import catt.mvp.sample.R
import catt.mvp.sample.app.interfaces.IMainActivityIFS
import catt.mvp.sample.app.master.MainActivity
import catt.mvp.sample.base.function.component.*
import catt.mvp.sample.base.proxy.ProxyBaseActivity
import catt.mvp.sample.base.proxy.annotations.InjectPresenter
import catt.mvp.sample.model.User
import org.android.eventbus.EventBus
import org.android.eventbus.Subscriber

@InjectPresenter(value = "catt.mvp.sample.presenter.MainActivityPresenter")
class MainActivityImpl : ProxyBaseActivity<MainActivity>(), IMainActivityIFS.View {

    private val _TAG: String by lazy { MainActivityImpl::class.java.simpleName }

    override fun onCreate() {
        super.onCreate()
        e(_TAG, "onCreate: ${R.id.container_layout}, lifecycle.state = $currentLifecycleState")
        val newInstanceOrigin = MainFragmentImpl::class.java.newInstanceOrigin<Fragment>()


        e(_TAG, "newInstanceOrigin::class.java=${newInstanceOrigin::class.java}")

        fragmentTransaction?.commitFragment(R.id.container_layout, MainFragmentImpl::class.java.newInstanceOrigin())
        EventBus.getDefault().register(this)
    }

    override fun onContent(content: String) {
        e(_TAG, "######   content = $content")
        val imageView: ImageView? = null
        context?.toastSuccess("aAAAA")
    }

    @Subscriber(tag = MainDialogFragmentImpl.EVENT_BUS_TAG)
    private fun getUser(user:User){
        e(_TAG, "username=${user.username}, age=${user.age}, identity=${user.identity}")
        context?.toastSuccess("username=${user.username}, age=${user.age}, identity=${user.identity}")
    }

    override fun onGrantedPermissionCompleted() {
        e(_TAG, "onGrantedPermissionCompleted: ")
        target?.toastSuccess("权限已全部授权")
    }

    override fun onViewLoadCompleted() {
        e(_TAG, "onViewLoadCompleted: lifecycle.state = $currentLifecycleState")
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}