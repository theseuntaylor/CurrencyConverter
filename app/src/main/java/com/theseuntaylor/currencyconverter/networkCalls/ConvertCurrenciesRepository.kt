package com.theseuntaylor.currencyconverter.networkCalls

import androidx.lifecycle.MutableLiveData
import com.theseuntaylor.currencyconverter.models.convertCurrencyModel.ConvertCurrencyResponse
import com.theseuntaylor.currencyconverter.networkCalls.retrofit.CurrenciesInterface
import com.theseuntaylor.currencyconverter.networkCalls.retrofit.ServiceBuilder
import com.theseuntaylor.currencyconverter.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ConvertCurrenciesRepository {

    val TAG = "Convert Currency"
    var fromValue: MutableLiveData<Resource<ConvertCurrencyResponse>> = MutableLiveData()

    fun convertCurrencies(
        from: String,
        to: String,
        amount: Double
    ): MutableLiveData<Resource<ConvertCurrencyResponse>> {

        val convertCurrency = ServiceBuilder.getCurrencyList(CurrenciesInterface::class.java)
        val call = convertCurrency.convertCurrencies(from, to, amount)

        call.enqueue(object : Callback<ConvertCurrencyResponse> {
            override fun onResponse(
                call: Call<ConvertCurrencyResponse>,
                response: Response<ConvertCurrencyResponse>
            ) {
                if (response.body() != null) {
                    val body: ConvertCurrencyResponse = response.body()!!
                    fromValue.value = Resource.success(body)

                }

            }

            override fun onFailure(call: Call<ConvertCurrencyResponse>, t: Throwable) {
                fromValue.value = Resource.error(t.message, null)
            }

        })

        return fromValue

    }

}