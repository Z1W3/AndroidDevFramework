package catt.mvp.sample.presenter

import catt.mvp.sample.app.interfaces.IMainActivityIFS
import catt.mvp.sample.base.model.network.base.OkRft
import catt.mvp.sample.base.model.network.callback.SimpleCallResult
import catt.mvp.sample.base.model.network.component.callJsonArrayResponse
import catt.mvp.sample.base.model.network.component.callJsonObjectResponse
import catt.mvp.sample.base.presenter.BasePresenter
import catt.mvp.sample.model.network.IDggStoreService
import catt.mvp.sample.model.network.response.LotteryListBean
import catt.mvp.sample.model.network.response.LotteryTypesBean
import okhttp3.ResponseBody
import retrofit2.Call

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
        dggService.getLotteryTypes().callJsonArrayResponse(result = object : SimpleCallResult<Array<LotteryTypesBean>>(){
            override fun onAfterFailure(code: Int, call: Call<ResponseBody>, ex: Throwable) {
                super.onAfterFailure(code, call, ex)
            }

            override fun onCheckLocalWifi() {
            }

            override fun onFailure2(code: Int, ex: Throwable) {
                super.onFailure2(code, ex)
            }

            override fun onResponse(response: Array<LotteryTypesBean>) {
                println("response.size = ${response.size}")
            }
        }, coroutine = this@MainActivityPresenter)


        dggService.getLotteryList("1", 0).callJsonObjectResponse(result = object : SimpleCallResult<LotteryListBean>(){
            override fun onCheckLocalWifi() {
            }

            override fun onFailure2(code: Int, ex: Throwable) {
                super.onFailure2(code, ex)
            }


            override fun onResponse(response: LotteryListBean) {
                println("onResponse: $response")
            }
        }, coroutine = this@MainActivityPresenter)
    }


    override fun onDestroy() {

    }

}