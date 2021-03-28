package com.theseuntaylor.currencyconverter.models.convertCurrencyModel

import com.google.gson.annotations.SerializedName

class Motd {

    @SerializedName("msg")
    var message: String? = null

    @SerializedName("url")
    var url: String? = null

}
