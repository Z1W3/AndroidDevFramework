package catt.mvp.sample.presenter

import catt.mvp.sample.app.interfaces.IMainActivity
import catt.mvp.sample.base.model.network.base.OkRetrofit
import catt.mvp.sample.base.model.network.callback.SimpleCallResult
import catt.mvp.sample.base.model.network.component.callJsonArrayResponse
import catt.mvp.sample.base.model.network.component.callJsonObjectResponse
import catt.mvp.sample.base.presenter.BasePresenter
import catt.mvp.sample.model.network.IDggStoreService
import catt.mvp.sample.model.network.response.LotteryListBean
import catt.mvp.sample.model.network.response.LotteryTypesBean

class MainActivityPresenter : BasePresenter(), IMainActivity.Presenter {
    override fun onCreate() {

    }

    private val _TAG:String by lazy { MainActivityPresenter::class.java.simpleName }

    private val dggService:IDggStoreService by lazy { OkRetrofit.create(IDggStoreService::class.java) }

    override fun setContent() {
        dggService.getLotteryTypes().callJsonArrayResponse(result = object : SimpleCallResult<Array<LotteryTypesBean>>(){
            override fun onCheckLocalWifi() {
            }


            override fun onFailure(code: Int, ex: Throwable) {
                super.onFailure(code, ex)
            }

            override fun onResponse(response: Array<LotteryTypesBean>) {
                println("response.size = ${response.size}")
            }
        }, coroutine = this@MainActivityPresenter)


        dggService.getLotteryList("1", 0).callJsonObjectResponse(result = object : SimpleCallResult<LotteryListBean>(){
            override fun onCheckLocalWifi() {
            }

            override fun onFailure(code: Int, ex: Throwable) {
                super.onFailure(code, ex)
            }

            override fun onResponse(response: LotteryListBean) {
                println("onResponse: $response")
                getViewInterface<IMainActivity.View>().onContent("AAAAAAAAAAAAAA")
            }
        }, coroutine = this@MainActivityPresenter)
    }


    override fun onDestroy() {

    }

}