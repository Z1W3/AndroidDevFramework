package catt.mvp.sample.model.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.HTTP


interface IDggStoreService{

    @HTTP(method = "GET", path = "xxx/yyy/zzz", hasBody = false)
    fun fetchLotteryTypes(): Call<ResponseBody>

    @HTTP(method = "GET", path = "xxx/yyy/zzz?shopType={lotteryTypeId}&ticketId={shopType}", hasBody = false)
    fun fetchLotteryList(lotteryTypeId: String = "", shopType: Int = 0): Call<ResponseBody>
}