package catt.mvp.sample.base.model.network.throwables

class ResponseBodyException(message: String? = null, cause: Throwable? = null) : IllegalArgumentException(message, cause)