package catt.mvp.sample.model.network.response

import catt.mvp.sample.model.network.response.base.IResponseBody
import catt.mvp.sample.model.network.response.model.LotteryListBean

class LotteryListResponse(override var code: Int = -1,
                          override var msg: String = "",
                          override var data: LotteryListBean? = LotteryListBean(),
                          override var timestamp: String) :
    IResponseBody<LotteryListBean>