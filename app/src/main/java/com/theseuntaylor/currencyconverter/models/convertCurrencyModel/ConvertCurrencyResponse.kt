package com.theseuntaylor.currencyconverter.models.convertCurrencyModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ConvertCurrencyResponse : Serializable {

    @SerializedName("motd")
    var motd: Motd? = null

    @SerializedName("success")
    var success: Boolean = false

    @SerializedName("query")
    var query: Query? = null

    @SerializedName("info")
    var info: Info? = null

    @SerializedName("historical")
    var historical: Boolean = false

    @SerializedName("date")
    var date: String? = null

    @SerializedName("result")
    var result: Double? = null

}