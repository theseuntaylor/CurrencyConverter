package com.theseuntaylor.currencyconverter.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.internal.LinkedTreeMap
import com.theseuntaylor.currencyconverter.R
import com.theseuntaylor.currencyconverter.rest.CurrenciesInterface
import com.theseuntaylor.currencyconverter.rest.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {

    val tag: String = "MainActivity TAG"

    private lateinit var toSpinner: Spinner
    private lateinit var toTextView: TextView

    private lateinit var toEditText: EditText
    private lateinit var fromEditText: EditText

    private lateinit var fromSpinner: Spinner
    private lateinit var fromTextView: TextView

    private lateinit var swapButton: ImageButton
    private lateinit var convertButton: Button

    private var topCurrency = ""
    private var bottomCurrency = ""

    var currencies: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViews()
        setViews()
        swapViews(swapButton, fromSpinner, toSpinner)

        convertButton.setOnClickListener {

            val getSymbols = ServiceBuilder.getCurrencyList(CurrenciesInterface::class.java)
            val call = getSymbols.getSymbols()

            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {

                        val jsonString: String = response.body().toString()

                        var responseMap: HashMap<String, Any> = HashMap()
                        responseMap = Gson().fromJson(jsonString, responseMap.javaClass)

                        if (responseMap.isNotEmpty() && responseMap.containsKey("symbols")) {

                            var symbols: LinkedTreeMap<String, Any> =
                                responseMap["symbols"] as LinkedTreeMap<String, Any>

                            for ((key) in symbols) currencies.add(key)
                            setUpSpinners()

                            Log.e(tag, "onResponse: ${symbols.keys}", null)


                        }


                        Log.e(
                            tag,
                            "onResponse: the key values from the response map are ${responseMap.keys}",
                            null
                        )

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })


//            if (topCurrency.isEmpty() || bottomCurrency.isEmpty()) {
//
//                Utils.showToast("Please put in a valid input", this)
//                return@setOnClickListener
//            }
//
//            val fromValue = fromEditText.text.toString()
//
//            if (fromValue.isEmpty()) {
//                Utils.showToast("Please put in a valid input", this)
//            } else {
//                convertCurrencies(fromValue)
//            }
        }
    }

    private fun setUpSpinners() {

        val locales = Locale.getAvailableLocales()
        val currencyLocales = Currency.getAvailableCurrencies()

        val countries: ArrayList<String> = ArrayList()
        //val currencies: ArrayList<String> = ArrayList()

//        for (currency in currencyLocales) run {
//
//            val currency: String = currency.currencyCode
//            if (currency.trim().isNotEmpty() && !currencies.contains(currency)) currencies.add(
//                currency
//            )
//
//        }

        for (locale: Locale in locales) run {

            val country: String = locale.displayCountry

            if (country.trim().isNotEmpty() && !countries.contains(country)) countries.add(country)

        }

        countries.sort()
        currencies.sort()

//        for (country: String in countries) run {
//            Log.e(TAG, "onCreate: country is: $country", null)
//        }
//
//        for (currency: String in currencies) run {
//            Log.e(TAG, "onCreate: currency is: $currency", null)
//        }

        val currencyAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item, currencies
        )

        val countryAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item, countries
        )

        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fromSpinner.adapter = currencyAdapter
        toSpinner.adapter = currencyAdapter

/*         ArrayAdapter.createFromResource(
            this,
            R.array.from_currencies,
            android.R.layout.simple_dropdown_item_1line
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            fromSpinner.adapter = adapter
        }
        ArrayAdapter.createFromResource(
            this,
            R.array.to_currencies,
            android.R.layout.simple_dropdown_item_1line
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            toSpinner.adapter = adapter
        }
*/
    }

    private fun setViews() {
        toSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                toTextView.text = ""
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val text = parent?.getItemAtPosition(position).toString()
                toTextView.text = text
                bottomCurrency = text
            }
        }

        fromSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                fromTextView.text = ""
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val text = parent?.getItemAtPosition(position).toString()
                fromTextView.text = text
                topCurrency = text


                Log.e(tag, "onItemSelected: item selected is $text", null)
                try {
                    Log.e(
                        tag,
                        "onItemSelected: item selected number is ${parent?.getPositionForView(view)}",
                        null
                    )
                } catch (e: Exception) {
                    Log.e(tag, "onItemSelected: $e", null)
                }
            }
        }
    }

    private fun convertCurrencies(fromValue: String) {

        val fromValueToDouble = fromValue.toDouble()

        val request = ServiceBuilder.buildService(CurrenciesInterface::class.java)
        val call = request.convertCurrency(topCurrency, bottomCurrency)

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {

                if (response.isSuccessful) {

                    val jsonObj = response.body()

                    val errorBodyJson = Gson().toJson(response.errorBody())
                    Log.e(tag, "onResponse: response body: ${jsonObj.toString()}", null)

                    try {
                        jsonObj?.apply {
                            val result = get(bottomCurrency)

                            Log.e(
                                tag,
                                "onResponse: result is: ${fromValueToDouble.times(result.asDouble)}",
                                null
                            )
                            Log.e(
                                tag,
                                "onResponse: result is: ${
                                    (BigDecimal(fromValueToDouble) * BigDecimal(result.asDouble)).setScale(
                                        3,
                                        BigDecimal.ROUND_HALF_UP
                                    )
                                }",
                                null
                            )

                            toEditText.setText(
                                String.format(
                                    (BigDecimal(fromValueToDouble) * BigDecimal(result.asDouble)).setScale(
                                        7,
                                        BigDecimal.ROUND_HALF_UP
                                    )
                                        .toString()
                                )
                            )
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.d(tag, e.localizedMessage)
                    }

                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                call.cancel()

                Log.e(tag, "onFailure: It failed!", t)

            }

        })
    }

    private fun swapViews(swapButtons: ImageButton, fromSpinner: Spinner, toSpinner: Spinner) {

        swapButtons.setOnClickListener {
            val item1: Int = fromSpinner.selectedItemPosition
            val item2: Int = toSpinner.selectedItemPosition

            toSpinner.setSelection(item1)

            fromSpinner.setSelection(item2)

        }
    }

    private fun findViews() {
        toEditText = findViewById(R.id.currency_to_editText)
        fromEditText = findViewById(R.id.currency_from_editText)
        convertButton = findViewById(R.id.convert_button)
        toSpinner = findViewById(R.id.toSpinner)
        toTextView = findViewById(R.id.currency_to_textView)
        fromSpinner = findViewById(R.id.fromSpinner)
        fromTextView = findViewById(R.id.currency_from_textView)
        swapButton = findViewById(R.id.swap_spinners_button)
    }

}