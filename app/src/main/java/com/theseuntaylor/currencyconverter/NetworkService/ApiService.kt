package com.theseuntaylor.currencyconverter.NetworkService

import com.theseuntaylor.currencyconverter.BuildConfig
import com.theseuntaylor.currencyconverter.Model.CurrencyConversionResponse
import com.theseuntaylor.currencyconverter.Model.CurrencyConverterResponse
import com.theseuntaylor.currencyconverter.Model.CurrencyLiveConversionResponse
import com.theseuntaylor.currencyconverter.Model.CurrencySymbolResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber
import java.util.concurrent.TimeUnit

interface ApiService {//@Query("access_key") key: String,

    @GET("/symbols")
    suspend fun getCurrencySymbol(): retrofit2.Response<CurrencySymbolResponse>

    @GET("/convert")
    suspend fun getConvertedExchange(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Int,
    ): retrofit2.Response<CurrencyConversionResponse>


    /**
     * Companion object to create the GithubApiService
     */
    companion object Factory {
        private val client = OkHttpClient
            .Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5,TimeUnit.MINUTES)
            .readTimeout(5,TimeUnit.MINUTES)
            //.addInterceptor(RequestInterceptor())
            .addInterceptor(getLoggerInterceptor())
            .build()

        private const val BASE_URL: String = "https://api.exchangerate.host/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java);
        }
    }

    class RequestInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val originalHttpUrl = original.url

            val url = originalHttpUrl.newBuilder()
                //.addQueryParameter("access_key", BuildConfig.API_KEY)
                //.addQueryParameter("apiKey", BuildConfig.API_KEY)
                .build()

            var newUrl = url.toString()
            newUrl = newUrl.replace("%2C", ",")

            val request = original.newBuilder().url(newUrl).build()
            return chain.proceed(request)
        }

    }
}

