package com.theseuntaylor.currencyconverter.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.theseuntaylor.currencyconverter.Model.*
import com.theseuntaylor.currencyconverter.Repository.CurrencyConverterRepository
import kotlinx.coroutines.*
import java.lang.reflect.Type

class CurrencyCoverterVM(application: Application) : AndroidViewModel(application) {
    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = SupervisorJob()

    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     *
     * Since we pass viewModelJob, you can cancel all coroutines launched by uiScope by calling
     * viewModelJob.cancel()
     */
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var currencyConverterRepository : CurrencyConverterRepository

    private var _mCurrencyConverterResponse: MutableLiveData<Event<Result>>

    var  mCurrencyConverterResponse: LiveData<Event<Result>>

    private var _mCurrencyConversionResponse: MutableLiveData<Event<ConversionResult>>

    var  mCurrencyConversionResponse: LiveData<Event<ConversionResult>>

    private var _mCurrencySymbolResponse: MutableLiveData<Event<SymbolResult>>

    var  mCurrencySymbolResponse: LiveData<Event<SymbolResult>>

    /**
     * init{} is called immediately when this ViewModel is created.
     */
     init {

        currencyConverterRepository = CurrencyConverterRepository.getInstance(application)

        _mCurrencyConverterResponse = MutableLiveData()
        mCurrencyConverterResponse = _mCurrencyConverterResponse

        _mCurrencyConversionResponse = MutableLiveData()
        mCurrencyConversionResponse = _mCurrencyConversionResponse

        _mCurrencySymbolResponse =  MutableLiveData()
        mCurrencySymbolResponse = _mCurrencySymbolResponse

    }


    fun convertAmount(currencies: String,
                      amount: Int){
        viewModelScope.launch {
            val result = currencyConverterRepository.getExchangeResult(currencies,amount)
            _mCurrencyConverterResponse.postValue(Event(result))
        }
    }

    fun getSymbol(){
        viewModelScope.launch {
            val result = currencyConverterRepository.getSymbolResult()
            _mCurrencySymbolResponse.postValue(Event(result))
        }
    }

    fun convertAmountAndGetRate(from: String, to: String, amount: Int){
        viewModelScope.launch {
            val result = currencyConverterRepository.getConversionResult(from, to, amount)
            _mCurrencyConversionResponse.postValue(Event(result))
        }
    }



    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}


