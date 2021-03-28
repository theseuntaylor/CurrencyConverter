package com.theseuntaylor.currencyconverter.models.convertCurrencyModel

import com.google.gson.annotations.SerializedName

class Query {

    @SerializedName("from")
    var fromCurrency: String? = null
    @SerializedName("to")
    var toCurrency: String? = null
    @SerializedName("amount")
    var amount: Double? = null

}
