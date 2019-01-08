package catt.mvp.sample.base.model.network.callback

import okhttp3.ResponseBody
import retrofit2.Call

open class SimpleCallResult<T> : ICallResult<T>{


    override fun onAfterFailure(code: Int, call: Call<ResponseBody>, ex: Throwable) {

    }

    override fun onCheckLocalWifi() {

    }

    override fun onFailure2(code: Int, ex: Throwable) {
        super.onFailure2(code, ex)
    }

    override fun onFailure(ex: Throwable) {
        super.onFailure(ex)
    }

    override fun onBeforeResponse() {
    }

    override fun onResponse(response: T) {
    }

}