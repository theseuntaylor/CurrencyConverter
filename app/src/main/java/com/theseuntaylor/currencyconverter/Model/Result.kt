package com.theseuntaylor.currencyconverter.Model

import okhttp3.ResponseBody

sealed class Result {
    data class Success(val response: Double) : Result()
    data class NetworkError(val response: Boolean): Result()
    data class Failure(val response: ResponseBody.Companion) : Result()
    data class Exception(val t: Throwable): Result()
}

sealed class SymbolResult {
    data class Success(val response: CurrencySymbolResponse) : SymbolResult()
    data class NetworkError(val response: Boolean): SymbolResult()
    data class Failure(val response: ResponseBody?) : SymbolResult()
    data class Exception(val t: Throwable): SymbolResult()
}

sealed class ConversionResult {
    data class Success(val response: CurrencyConversionResponse) : ConversionResult()
    data class NetworkError(val response: Boolean):ConversionResult()
    data class Failure(val response: ResponseBody?) : ConversionResult()
    data class Exception(val t: Throwable): ConversionResult()
}


