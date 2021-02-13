package com.theseuntaylor.currencyconverter.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ViewModelFactory(val app: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyCoverterVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CurrencyCoverterVM(app)  as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }

}