package com.theseuntaylor.currencyconverter.utils

class Resource<T> private constructor(
    val status: Status,
    val message: String?,
    val data: T?
) {
    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, null, data)
        }

        fun <T> error(msg: String?): Resource<T> {
            return Resource(Status.ERROR, msg, null)
        }

        fun <T> loading(data: T): Resource<T> {
            return Resource(Status.LOADING, null, data)
        }
    }
}