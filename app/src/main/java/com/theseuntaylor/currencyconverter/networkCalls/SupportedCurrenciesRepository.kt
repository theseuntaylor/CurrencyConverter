package com.theseuntaylor.currencyconverter.networkCalls

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.internal.LinkedTreeMap
import com.theseuntaylor.currencyconverter.networkCalls.retrofit.CurrenciesInterface
import com.theseuntaylor.currencyconverter.networkCalls.retrofit.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SupportedCurrenciesRepository {

    val TAG: String = "Currency Repo"
    var currencies: MutableLiveData<ArrayList<String>>? = MutableLiveData<ArrayList<String>>()

    var anotherCurrencyList: ArrayList<String> = ArrayList()

    fun getSupportedCurrencies(): MutableLiveData<ArrayList<String>>? {

        val getSymbols = ServiceBuilder.getCurrencyList(CurrenciesInterface::class.java)
        val call = getSymbols.getSymbols()

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {

                    val jsonString: String = response.body().toString()

                    var responseMap: HashMap<String, Any> = HashMap()
                    responseMap = Gson().fromJson(jsonString, responseMap.javaClass)

                    if (responseMap.isNotEmpty() && responseMap.containsKey("symbols")) {

                        val symbols: LinkedTreeMap<String, Any> =
                            responseMap["symbols"] as LinkedTreeMap<String, Any>

                        for ((key) in symbols) {
                            anotherCurrencyList.add(key)
                        }
                        currencies?.value = anotherCurrencyList


                        Log.e(TAG, "onResponse: ${symbols.keys}", null)
                        Log.e(TAG, "onResponse:  $anotherCurrencyList", null)

                    }

                    Log.e(
                        TAG,
                        "onResponse: the key values from the response map are ${responseMap.keys}",
                        null
                    )

                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                Log.e(TAG, "onFailure: ${t.printStackTrace()}", t)

                try {
                    currencies?.value = null
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        })

        Log.e(TAG, "getSupportedCurrencies: ${currencies?.value}", null)
        return currencies

    }

}