package com.theseuntaylor.currencyconverter.Model

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class Constanst{
    companion object{
    lateinit var valueName: String
}
}
data class CurrencyConverterResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("query")
    val query: QueryResponse,

    @SerializedName("info")
    val info: InfoResponse,

    @SerializedName("historical")
    val historical: Boolean,

    @SerializedName("date")
    val date: DateFormat,

    @SerializedName("result")
    val result: Double,


    var error: ErrorBodyModel?

)

data class CurrencyLiveConversionResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("terms")
    val terms: String,

    @SerializedName("privacy")
    val privacy: String,

    @SerializedName("timestamp")
    val timestamp: String,

    @SerializedName("source")
    val source: String,

    @SerializedName("quotes")
    val quotes: Quotes,

)

data class Quotes(val firstResultFromQuotes: Double, val secondResultFromQuotes: Double)

data class ConvertedExchange(
   val result: Map<String,Double>)

data class QueryResponse(
    @SerializedName("from")
    val from: String,

    @SerializedName("to")
    val to: String,

    @SerializedName("amount")
    val amount: Int,
)

data class InfoResponse(
    @SerializedName("timestamp")
    val timestamp: Timestamp? = null,

    @SerializedName("rate")
    val rate: Double,
)

class ErrorBodyModel(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("error")
    val error: ErrorModel )

data class ErrorModel(val code: Int, val info: String)
