package com.theseuntaylor.currencyconverter.Model

import okhttp3.ResponseBody

sealed class Result {
    data class Success(val response: Double) : Result()
    data class NetworkError(val response: Boolean): Result()
    data class Failure(val response: ResponseBody.Companion) : Result()
    data class Exception(val t: Throwable): Result()
}


