package com.theseuntaylor.currencyconverter.rest

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {

    private val client: OkHttpClient = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder().baseUrl("www.google.com")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(client).build()

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}