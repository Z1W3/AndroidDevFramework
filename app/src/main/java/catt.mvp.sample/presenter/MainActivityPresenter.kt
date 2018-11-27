package catt.mvp.sample.presenter

import android.util.Log.e
import android.util.Log.i
import catt.mvp.sample.app.interfaces.IMainActivityIFS
import catt.mvp.sample.base.mvp.model.network.OkRft
import catt.mvp.sample.base.mvp.presenter.BasePresenter
import catt.mvp.sample.model.network.IDggStoreService
import kotlinx.coroutines.*
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
        val call:Call<ResponseBody> = dggService.getLotteryTypes()
        call.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                e(_TAG, "onFailure", t)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                i(_TAG, "onResponse: code=${response.code()}, message=${response.message()}")
                val string = response.body()?.string()
                launch(Dispatchers.Main, CoroutineStart.ATOMIC) {
                    viewIFS?.onContent(string!!)
                }
            }
        })
    }


    override fun onDestroy() {

    }

}