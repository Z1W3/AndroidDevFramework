package catt.mvp.sample.model.network.response

import catt.mvp.sample.base.model.network.resopnse.JsonTargetDataField
import catt.mvp.sample.base.model.network.resopnse.JsonField
import com.google.gson.annotations.SerializedName

@JsonField
//parent data
//data
@JsonTargetDataField("accountVo/userVo/nameVos")
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
}