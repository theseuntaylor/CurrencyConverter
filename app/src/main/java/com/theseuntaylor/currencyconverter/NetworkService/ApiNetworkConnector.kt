package com.theseuntaylor.currencyconverter.NetworkService

import com.theseuntaylor.currencyconverter.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

object ApiNetworkConnector {

    private var retrofit: Retrofit? = null
    private val client = OkHttpClient.Builder()
        .addInterceptor(RequestInterceptor())
        .addInterceptor(getLoggerInterceptor())
        .build()
    //private const val BASE_URL: String = "http://data.fixer.io/api/"
    //private const val BASE_URL: String = "http://api.currencylayer.com/"
    private const val BASE_URL: String = "https://free.currconv.com/"


    init {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Create a Singleton class that uses an instance throughout
    /*private fun getRetrofitInstance(): Retrofit? {
        if(retrofit == null){
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        return retrofit
    }*/


    fun<T> buildService(service: Class<T>): T? {
        if (retrofit == null){
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit?.create(service)
    }

    private fun getLoggerInterceptor(): Interceptor {
        val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Timber.d(it)
        })
        logger.level = HttpLoggingInterceptor.Level.BASIC
        return logger
    }
}

class RequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url

        val url = originalHttpUrl.newBuilder()
            //.addQueryParameter("access_key", BuildConfig.API_KEY)
            .addQueryParameter("apiKey", BuildConfig.API_KEY)
            .build()

        var newUrl = url.toString()
        newUrl = newUrl.replace("%2C",",")

        val request = original.newBuilder().url(newUrl).build()
        return chain.proceed(request)
    }

}

