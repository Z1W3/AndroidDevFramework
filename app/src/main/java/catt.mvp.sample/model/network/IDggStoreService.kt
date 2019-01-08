package catt.mvp.sample.model.network

import catt.mvp.sample.model.network.request.BlogBean
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HTTP


interface IDggStoreService{

    @HTTP(method = "GET", path = "ticket/loadAppTicket/ticketTypeList", hasBody = false)
    fun getLotteryTypes(): Call<ResponseBody>

    @HTTP(method = "GET", path = "ticket/loadAppTicket/lotteryList?shopType={lotteryTypeId}&ticketId={shopType}", hasBody = false)
    fun getLotteryList(lotteryTypeId: String = "", shopType: Int = 0): Call<ResponseBody>

    @HTTP(method = "POST", path = "xxx/yyy/zzz", hasBody = true)
    fun postCreateBlog(@Body blog: BlogBean): Call<Result<BlogBean>>
}