package com.theseuntaylor.currencyconverter.rest

import com.theseuntaylor.currencyconverter.models.convertCurrencyModel.CurrencyResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrenciesInterface {

    @GET("")
    fun convertCurrency(
        @Query("fsym") fsym: String,
        @Query("tsyms") tsyms: String
    ): Call<CurrencyResponse>
}