package com.theseuntaylor.currencyconverter

import android.app.Application
import timber.log.Timber

class CurrencyConverterApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}