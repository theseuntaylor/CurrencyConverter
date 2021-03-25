package com.theseuntaylor.currencyconverter.rest

import com.google.gson.JsonObject
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
}