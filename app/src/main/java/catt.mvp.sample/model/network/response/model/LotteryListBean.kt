package catt.mvp.sample.model.network.response.model

import com.google.gson.annotations.SerializedName


class LotteryListBean {

    /**
     * 竖图
     */
    @SerializedName("leftImg")
    var verticalImage: String = ""

    /**
     * 面值
     */
    var parValue: Float = 0F

    /**
     * 票/包列表
     */
    @SerializedName("ticketList")
    var list : Array<String> = arrayOf()
}