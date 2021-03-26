package com.theseuntaylor.currencyconverter.Model

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp
import java.text.DateFormat


data class CurrencySymbolResponse(
    @SerializedName("motd")
    val motd: Motd,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("symbols")
    val symbols: Map<String, SymbolDescription>

)

//data class TheSymbol(List<Symbols>)


data class Symbols(
    @SerializedName("anything",alternate = ["",""])
    val anything: List<Map<String, SymbolDescription>>
)

data class SymbolDescription(
    @SerializedName("description")
    val description: String,

    @SerializedName("code")
    val code: String
)

data class Motd(
    @SerializedName("msg")
    val msg: String,

    @SerializedName("url")
    val url: String
)

data class CurrencyConversionResponse(
    @SerializedName("motd")
    val motd: Motd,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("query")
    val query: QueryResponse,

    @SerializedName("info")
    val info: InfoResponse,

    @SerializedName("historical")
    val historical: Boolean,

    @SerializedName("date")
    val date: String,

    @SerializedName("result")
    val result: Double
)