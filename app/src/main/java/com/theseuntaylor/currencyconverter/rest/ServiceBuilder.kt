package com.theseuntaylor.currencyconverter.rest

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {

    private const val crypto_compare_base_url = "https://min-api.cryptocompare.com/"
    private const val exchange_rates_base_url = "https://api.exchangerate.host/"

    private val client: OkHttpClient =
        OkHttpClient.Builder().addInterceptor(getInterceptor()).build()

    private val retrofit = Retrofit.Builder().baseUrl(crypto_compare_base_url)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(client).build()

    private val retrofit2 = Retrofit.Builder().baseUrl(exchange_rates_base_url)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(client).build()

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }

    fun <T> getCurrencyList(service: Class<T>): T {
        return retrofit2.create(service)
    }

    private fun getInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }
}