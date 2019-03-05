package catt.mvp.sample.model.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Connection", "keep-alive")
                .addHeader("authorization" /*认证用户token*/, "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJ1c2VyUm9sZVwiOlwibm9ybWFsXCIsXCJ1c2VySWRcIjpcIjRjNDM2MjdhMmYwYTRkZWFiNTg3NmY5YTdjZGJlZWY0XCJ9IiwiZXhwIjoxNTc0NzU2NzEzfQ.ix-1aaIbQh9pyDO2ZxK2sENy9u6iaAd4BhxpKxi8XGs")
                .addHeader("beginTime"/*请求开始时间*/, System.currentTimeMillis().toString())
                .addHeader("appId"/*客户端APP编号*/, "5b1754733cb3f37bb05bb0f5")
                .addHeader("terminal"/*客户端版本*/, "")
                .addHeader("deviceNo"/*设备号*/, "D428D53116A0616")
                .build()
        )
}