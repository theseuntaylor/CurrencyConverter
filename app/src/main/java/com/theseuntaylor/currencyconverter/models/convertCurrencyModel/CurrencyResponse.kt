package com.theseuntaylor.currencyconverter.models.convertCurrencyModel

import java.io.Serializable

class CurrencyResponse : Serializable {

    var result: Double? = null

}


data class response(
    val USD: Double
)