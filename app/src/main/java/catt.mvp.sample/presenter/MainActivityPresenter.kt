package catt.mvp.sample.presenter

import android.util.Log
import catt.mvp.sample.app.interfaces.IMainActivityIFS
import catt.mvp.sample.base.model.network.OkRft
import catt.mvp.sample.base.presenter.BasePresenter
import catt.mvp.sample.model.network.IDggStoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class MainActivityPresenter : BasePresenter<IMainActivityIFS.View>(), IMainActivityIFS.Presenter {
    private val _TAG:String by lazy { MainActivityPresenter::class.java.simpleName }

    private val dggService:IDggStoreService by lazy { OkRft.create(IDggStoreService::class.java) }

    init {
        Thread{
            Thread.sleep(1000L)
            setContent()
        }.start()
    }

    override fun setContent() {
        launch (Dispatchers.Unconfined) {
            dggService.getLotteryTypes().enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.e(_TAG, "body=${response.body()?.string()}")
                    val string = response.body()?.string()
                    async(Dispatchers.Main) {
                        viewIFS?.onContent(string!!)
                    }
                }
            })
        }
    }

    override fun onDestroy() {

    }

}