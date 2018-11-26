package catt.mvp.sample.model.network.response

import catt.mvp.sample.model.network.response.base.IResponseBody
import catt.mvp.sample.model.network.response.model.LotteryTypesBeans

class LotteryTypesResponse(override var code: Int = -1,
                           override var msg: String = "",
                           override var data: LotteryTypesBeans? = LotteryTypesBeans(),
                           override var timestamp: String) :
    IResponseBody<LotteryTypesBeans>