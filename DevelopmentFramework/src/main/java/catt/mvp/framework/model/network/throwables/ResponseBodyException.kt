package catt.mvp.framework.model.network.throwables

class ResponseBodyException(message: String? = null, cause: Throwable? = null) : IllegalArgumentException(message, cause)