package com.theseuntaylor.currencyconverter.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.theseuntaylor.currencyconverter.models.convertCurrencyModel.ConvertCurrencyResponse
import com.theseuntaylor.currencyconverter.networkCalls.ConvertCurrenciesRepository
import com.theseuntaylor.currencyconverter.networkCalls.SupportedCurrenciesRepository
import com.theseuntaylor.currencyconverter.utils.Resource


class MainActivityViewModel : ViewModel() {

    private var repository = SupportedCurrenciesRepository
    private var convertRepository = ConvertCurrenciesRepository

    var conversionResponse:
            MutableLiveData<Resource<ConvertCurrencyResponse>> =
        MutableLiveData<Resource<ConvertCurrencyResponse>>()

    var listOfCurrencies: MutableLiveData<ArrayList<String>>? = MutableLiveData<ArrayList<String>>()

    var currencies = java.util.ArrayList<String>()

    fun getConvertedCurrencies(
        from: String,
        to: String,
        amount: Double
    ): MutableLiveData<Resource<ConvertCurrencyResponse>> {
        conversionResponse = loadConversion(from, to, amount)
        return conversionResponse
    }

    private fun loadConversion(
        from: String,
        to: String,
        amount: Double
    ): MutableLiveData<Resource<ConvertCurrencyResponse>> {
        return convertRepository.convertCurrencies(from, to, amount)
    }

    private fun loadCurrencies(): MutableLiveData<ArrayList<String>>? {
        return repository.getSupportedCurrencies()
    }

    fun getCurrencies(): MutableLiveData<ArrayList<String>>? {
        listOfCurrencies = loadCurrencies()
        return listOfCurrencies
    }
}