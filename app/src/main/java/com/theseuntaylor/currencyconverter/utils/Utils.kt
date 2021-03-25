package com.theseuntaylor.currencyconverter.utils

import android.content.Context
import android.widget.Toast

object Utils {

    fun showToast(message: String, context: Context) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show()

    }

}