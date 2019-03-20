package catt.mvp.framework.model.network.callback

import okhttp3.ResponseBody
import retrofit2.Call

open class SimpleCallResult<T> : ICallResult<T>{


    override fun onAfterFailure(code: Int, call: Call<ResponseBody>, ex: Throwable) {

    }

    override fun onCheckLocalWifi() {

    }

    override fun onFailure(code: Int, ex: Throwable) {
        super.onFailure(code, ex)
    }

    override fun onBeforeResponse() {
    }

    override fun onResponse(response: T) {
    }

}