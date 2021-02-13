package com.theseuntaylor.currencyconverter.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.theseuntaylor.currencyconverter.Model.CurrencyConverterResponse
import com.theseuntaylor.currencyconverter.Model.Result
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

    private var _mCurrencyConverterResponse: MutableLiveData<Result>

    var  mCurrencyConverterResponse: LiveData<Result>

    /**
     * init{} is called immediately when this ViewModel is created.
     */
     init {

        currencyConverterRepository = CurrencyConverterRepository.getInstance(application)

        _mCurrencyConverterResponse = MutableLiveData()
        mCurrencyConverterResponse = _mCurrencyConverterResponse

    }


    fun convertAmount(currencies: String,
                      amount: Int){
        viewModelScope.launch {

            val result = currencyConverterRepository.getExchangeResult(currencies,amount)
            _mCurrencyConverterResponse.postValue(result)
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


