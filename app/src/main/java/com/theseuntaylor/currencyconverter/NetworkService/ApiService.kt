package com.theseuntaylor.currencyconverter.NetworkService

import com.theseuntaylor.currencyconverter.Model.CurrencyConverterResponse
import com.theseuntaylor.currencyconverter.Model.CurrencyLiveConversionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    /*@GET("/convert")
    fun getConvertedCurrency(@Query("access_key") key: String,
                  @Query("from") fromCurrencyCode: String,
                  @Query("to") toCurrencyCode: String,
                  @Query("amount") amountToConvert: Int ): Call<CurrencyConverterResponse>*/

    @GET("/live")
    fun getConvertedCurrency(@Query("access_key") key: String,
                             @Query("currencies") currenciesYouWant: String,
                             @Query("format") format: Int = 1): Call<CurrencyLiveConversionResponse>

    @GET("/api/v7/convert")
    fun getConvertedExchange(@Query("q") q: String,
                                     @Query("compact") compact: String = "ultra"): Call<Map<String,Double>>
}