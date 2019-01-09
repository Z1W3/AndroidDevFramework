package catt.mvp.sample.model.network.response

import catt.mvp.sample.base.model.network.annotations.JsonCallField
import catt.mvp.sample.base.model.network.annotations.JsonCallDataTargetField

@JsonCallField
@JsonCallDataTargetField(hierarchy = "appTicketTypeVos")
class LotteryTypesBean {
    /**
     * 彩种id
     */
    var id: String = ""

    /**
     * logo
     */
    var logo: String = ""

    /**
     * 彩种名称
     */
    var label: String = ""

    /**
     * 彩种面值
     */
    var parValue: Float = 0F

    /**
     * 彩种货币种类
     */
    var currencyType: String = ""

    override fun toString(): String {
        return "LotteryTypesBean(id='$id', logo='$logo', label='$label', parValue=$parValue, currencyType='$currencyType')"
    }
}