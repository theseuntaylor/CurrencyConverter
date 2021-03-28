package com.theseuntaylor.currencyconverter.networkCalls.retrofit

import com.google.gson.JsonObject
import com.theseuntaylor.currencyconverter.models.convertCurrencyModel.ConvertCurrencyResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrenciesInterface {

    @GET("data/price")
    fun convertCurrency(
        @Query("fsym") fsym: String,
        @Query("tsyms") tsyms: String
    ): Call<JsonObject>

    @GET("symbols")
    fun getSymbols(): Call<JsonObject>

    @GET("convert")
    fun convertCurrencies(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double = 1.0
    ): Call<ConvertCurrencyResponse>


}