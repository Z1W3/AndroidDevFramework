package catt.mvp.sample.model.network.response.base

interface IResponseBody<T> {
    var code: Int
    var msg: String
    var data: T?
    var timestamp: String
}