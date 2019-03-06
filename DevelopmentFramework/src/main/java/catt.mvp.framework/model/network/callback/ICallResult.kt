package catt.mvp.framework.model.network.callback

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import catt.mvp.framework.R
import catt.mvp.framework.function.component.toastError
import catt.mvp.framework.globalContext
import catt.mvp.framework.isSPT
import catt.mvp.framework.model.network.throwables.ResponseBodyException
import okhttp3.ResponseBody
import retrofit2.Call
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface ICallResult<T> {



    /**
     * 判断网络是否未连接
     *
     * @return
     */
    private fun isNetworkAvailable(ctx:Context): Boolean {
        val conn:ConnectivityManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            conn.activeNetwork?:return false
            val networkCapabilities: NetworkCapabilities = conn.getNetworkCapabilities(conn.activeNetwork)
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            for (net in conn.allNetworkInfo) {
                if (net.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
            return false
        }
    }

    /**
     * 请求成功之前
     */
    fun onBeforeResponse()

    /**
     * 请求成功时
     *
     * @param response 结果数据
     */
    fun onResponse(response: T)

    /**
     * 请求失败后的处理
     */
    fun onAfterFailure(code: Int, call: Call<ResponseBody>, ex: Throwable)

    fun onCheckLocalWifi()

    /**
     * 请求失败时的处理
     *
     * @param ex
     */
    fun onFailure(code: Int, ex: Throwable) {
        displayToast(globalContext, code, ex)
    }

    private fun displayToast(ctx:Context, code: Int, ex: Throwable){
        when  {
            ex is SocketTimeoutException || ex is ConnectException -> {
                when (isNetworkAvailable(ctx)) {
                    true -> ctx.toastError(R.string.net_network_connection_not_smooth)
                    false ->{
                        ctx.toastError(R.string.net_local_wifi_error)
                        onCheckLocalWifi()
                    }
                }
            }
            ex is UnknownHostException->{
                when (isNetworkAvailable(ctx)) {
                    true -> ctx.toastError(
                        when (isSPT) {
                            true -> R.string.as_net_server_api_abnormal
                            false -> R.string.net_server_api_abnormal
                        }
                    )
                    false ->{
                        ctx.toastError(R.string.net_local_wifi_error)
                        onCheckLocalWifi()
                    }
                }
            }
            code == 0 && ex is ResponseBodyException -> ctx.toastError(R.string.as_net_server_api_abnormal)
            code == 401 && ex is ResponseBodyException -> ctx.toastError(ex.message!!)
            else -> {
                ctx.toastError(
                    when (isSPT) {
                        true -> R.string.as_net_server_api_abnormal
                        false -> R.string.net_server_api_abnormal
                    }
                )
            }
        }
    }
}