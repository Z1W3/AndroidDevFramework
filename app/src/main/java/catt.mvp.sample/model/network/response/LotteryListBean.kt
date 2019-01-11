package catt.mvp.sample.model.network.response

import catt.mvp.framework.model.network.annotations.JsonCallField
import com.google.gson.annotations.SerializedName
import java.util.*

@JsonCallField
class LotteryListBean{


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
    var list: Array<String> = arrayOf()


    override fun toString(): String {
        return "LotteryListBean(verticalImage='$verticalImage', parValue=$parValue, list=${Arrays.toString(list)})"
    }
}