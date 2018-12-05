package catt.mvp.sample.app.proxy

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import catt.mvp.sample.R
import catt.mvp.sample.app.interfaces.IMainDialogFragmentIFS
import catt.mvp.sample.app.master.MainDialogFragment
import catt.mvp.sample.base.proxy.ProxyBaseDialogFragment
import catt.mvp.sample.model.User
import catt.mvp.sample.presenter.MainDialogPresenter
import org.android.eventbus.EventBus

class MainDialogFragmentImpl
    : ProxyBaseDialogFragment<MainDialogFragment, IMainDialogFragmentIFS.View, MainDialogPresenter>(),
    IMainDialogFragmentIFS.View {


    private val _TAG : String by lazy { MainFragmentImpl::class.java.simpleName }

    override fun onViewLoadCompleted() {

    }

    override fun onContent(content: String) {
        Log.e(_TAG, "content = $content")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.start_activity_btn).setOnClickListener {
//            activity?.startActivity(Intent(activity, MainActivity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            })

            EventBus.getDefault().post(User("LuckyCatt", 30, 110101209900000019), EVENT_BUS_TAG)
        }
    }

    companion object {
        const val EVENT_BUS_TAG:String = "MY_EVENT_1"
    }
}