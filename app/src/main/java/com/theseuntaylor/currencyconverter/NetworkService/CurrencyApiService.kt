package com.theseuntaylor.currencyconverter.NetworkService

import com.theseuntaylor.currencyconverter.BuildConfig
import com.theseuntaylor.currencyconverter.Model.ConvertedExchange
import com.theseuntaylor.currencyconverter.Model.CurrencyLiveConversionResponse
import kotlinx.coroutines.Deferred
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

interface CurrencyApiService {

    //@Query("access_key") key: String,

    @GET("/live")
    suspend fun getConvertedCurrency(@Query("currencies") currenciesYouWant: String,
                                  @Query("format") format: Int = 1): retrofit2.Response<CurrencyLiveConversionResponse>

    @GET("/api/v7/convert")
    suspend fun getConvertedExchange(@Query("q") q: String,
                                     @Query("compact") compact: String = "ultra"): retrofit2.Response<Map<String,Double>>



    /**
     * Companion object to create the GithubApiService
     */
    companion object Factory {
        private val client = OkHttpClient
            .Builder()
            .addInterceptor(RequestInterceptor())
            .addInterceptor(getLoggerInterceptor())
            .build()
        //private const val BASE_URL: String = "http://data.fixer.io/api/"
        //private const val BASE_URL: String = "http://api.currencylayer.com/"
        private const val BASE_URL: String = "https://free.currconv.com/"
        fun create(): CurrencyApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(CurrencyApiService::class.java);
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
}

fun getLoggerInterceptor(): Interceptor {
    val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
        Timber.d(it)
    })
    logger.level = HttpLoggingInterceptor.Level.BASIC
    //logger.level = HttpLoggingInterceptor.Level.BODY
    logger.level = HttpLoggingInterceptor.Level.HEADERS

    return logger
}