package com.theseuntaylor.currencyconverter.Repository

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.theseuntaylor.currencyconverter.BuildConfig
import com.theseuntaylor.currencyconverter.Model.*
import com.theseuntaylor.currencyconverter.NetworkService.ApiNetworkConnector
import com.theseuntaylor.currencyconverter.NetworkService.ApiService
import com.theseuntaylor.currencyconverter.NetworkService.CurrencyApiService
import com.theseuntaylor.currencyconverter.Util.SingletonHolder
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class CurrencyConverterRepository private constructor(application: Application) {

    //private var instance = create()
    private var mContext: Context
    private var mApiService: CurrencyApiService
    private var mCurrencyApiService: ApiService
    private val coroutineContext: CoroutineContext get() = Dispatchers.IO

    init {
        this.mContext = application.applicationContext
        mApiService = CurrencyApiService.create()
        mCurrencyApiService = ApiService.create()
    }


    companion object :
        SingletonHolder<CurrencyConverterRepository, Application>(::CurrencyConverterRepository)

    /*fun getInstance(application: Application) : CurrencyConverterRepository {
        if (instance == null) {
            instance = CurrencyConverterRepository(application);
        }
        return instance;
    }*/


    suspend fun getExchangeResult(
        currencies: String,
        amount: Int
    ): Result {
        return withContext(Dispatchers.IO) {
            Timber.tag("I got here").d("Here we are")
            try {
                val response = mApiService.getConvertedExchange(currencies)
                if (response.isSuccessful) {
                    if(response.body() != null){
                        Timber.tag("Conversion Completed").d(response.message())
                        val convertedModel: Map<String, Double> = response.body()!!
                        val amountConverted = (amount * convertedModel.getValue(currencies))
                        return@withContext Result.Success(amountConverted)
                    }
                    else{
                     return@withContext Result.Failure(ResponseBody)
                    }
                } else {
                    return@withContext Result.NetworkError(true)
                }
            } catch (t: Throwable) {
                if (t !is CancellationException) {
                    return@withContext Result.Exception(t)
                } else {
                    throw t
                }
            }

        }

    }

    suspend fun getSymbolResult(
    ): SymbolResult {
        return withContext(Dispatchers.IO) {
            Timber.tag("I got here").d("Here we are")
            try {
                val response = mCurrencyApiService.getCurrencySymbol()
                if (response.isSuccessful) {
                    if(response.body() != null){
                        Timber.tag("Conversion Completed").d(response.message())
                        val convertedModel: CurrencySymbolResponse = response.body()!!
                        //val amountConverted = (amount * convertedModel.getValue(currencies))
                        return@withContext SymbolResult.Success(convertedModel)
                    }
                    else{
                        return@withContext SymbolResult.Failure(response.errorBody())
                    }
                } else {
                    return@withContext SymbolResult.NetworkError(true)
                }
            } catch (t: Throwable) {
                if (t !is CancellationException) {
                    return@withContext SymbolResult.Exception(t)
                } else {
                    throw t
                }
            }

        }

    }

    suspend fun getConversionResult(
        from: String,
        to: String,
        amount: Int
    ): ConversionResult {
        return withContext(Dispatchers.IO) {
            Timber.tag("I got here").d("Here we are")
            try {
                val response = mCurrencyApiService.getConvertedExchange(from, to, amount)
                if (response.isSuccessful) {
                    if(response.body() != null){
                        Timber.tag("Conversion Completed").d(response.message())
                        val convertedModel: CurrencyConversionResponse = response.body()!!
                        return@withContext ConversionResult.Success(convertedModel)
                    }
                    else{
                        return@withContext ConversionResult.Failure(response.errorBody())
                    }
                } else {
                    return@withContext ConversionResult.NetworkError(true)
                }
            } catch (t: Throwable) {
                if (t !is CancellationException) {
                    return@withContext ConversionResult.Exception(t)
                } else {
                    throw t
                }
            }

        }

    }


}
