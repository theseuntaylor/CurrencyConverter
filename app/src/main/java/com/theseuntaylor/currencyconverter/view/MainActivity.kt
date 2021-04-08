package com.theseuntaylor.currencyconverter.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.google.android.material.snackbar.Snackbar
import com.theseuntaylor.currencyconverter.R
import com.theseuntaylor.currencyconverter.utils.Resource
import com.theseuntaylor.currencyconverter.utils.Utils
import com.theseuntaylor.currencyconverter.viewModel.MainActivityViewModel
import java.util.*


class MainActivity : AppCompatActivity() {

    val tag: String = "MainActivity TAG"

    lateinit var context: Context
    lateinit var viewModel: MainActivityViewModel

    private lateinit var toEditText: EditText
    private lateinit var toSpinner: Spinner
    private lateinit var toTextView: TextView
    private lateinit var detailsTextView: TextView

    private lateinit var fromEditText: EditText
    private lateinit var fromSpinner: Spinner
    private lateinit var fromTextView: TextView

    private lateinit var swapButton: ImageButton
    private lateinit var convertButton: CircularProgressButton

    private var bottomCurrency = ""
    private var topCurrency = ""

    // private lateinit var currencyObserver: androidx.lifecycle.Observer<ArrayList<String>>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this@MainActivity
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        findViews()
        setViews()

        if (viewModel.currencies.isEmpty()) {
            viewModel.getCurrencies()?.observe(this, {
                viewModel.currencies.addAll(it)
                viewModel.currencies.sort()

                setUpSpinners()
            })

        } else {
            setUpSpinners()
        }

        swapViews(swapButton, fromSpinner, toSpinner)

        convertButton.setOnClickListener {

            if (topCurrency.isEmpty() || bottomCurrency.isEmpty()) {

                val snackBar =
                    Snackbar.make(it, "Please put in a valid input", Snackbar.LENGTH_LONG)
                val snackBarView = snackBar.view
                snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.snackBar_color
                    )
                )
                val textView =
                    snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView

                textView.setTextColor(ContextCompat.getColor(context, R.color.background_color))
                snackBar.show()

                return@setOnClickListener
            }

            val fromValue = fromEditText.text.toString()

            if (fromValue.isEmpty()) {

                val snackBar =
                    Snackbar.make(it, "Please put in a value (e.g. 1.6180)", Snackbar.LENGTH_LONG)
                val snackBarView = snackBar.view
                snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.snackBar_color
                    )
                )

                val textView =
                    snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(ContextCompat.getColor(context, R.color.background_color))
                snackBar.show()


            } else {
                convertButton.startMorphAnimation()

                convertCurrencies(fromValue)
            }
        }
    }


    private fun setUpSpinners() {


        val currencyAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, viewModel.currencies
        )

        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        fromSpinner.adapter = currencyAdapter
        toSpinner.adapter = currencyAdapter

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

        viewModel
            .getConvertedCurrencies(topCurrency, bottomCurrency, fromValueToDouble)
            .observe(this, {

                convertButton.revertAnimation()

                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        toEditText.setText(it.data?.result.toString())
                        detailsTextView.text =
                            "The rate being used is " +
                                    "${it.data?.info?.rate} " +
                                    "${it.data?.query?.toCurrency} to 1" +
                                    " ${it.data?.query?.fromCurrency}"
                    }
                    Resource.Status.LOADING -> {
                    }
                    Resource.Status.ERROR -> Utils.showToast("${it.data?.success}", this)
                }


            })

//        val request = ServiceBuilder.buildService(CurrenciesInterface::class.java)
//        val call = request.convertCurrency(topCurrency, bottomCurrency)
//
//        call.enqueue(object : Callback<JsonObject> {
//            override fun onResponse(
//                call: Call<JsonObject>,
//                response: Response<JsonObject>
//            ) {
//
//                if (response.isSuccessful) {
//
//                    val jsonObj = response.body()
//
////                    val errorBodyJson = Gson().toJson(response.errorBody())
//                    Log.e(tag, "onResponse: response body: ${jsonObj.toString()}", null)
//
//                    try {
//                        jsonObj?.apply {
//                            val result = get(bottomCurrency)
//
//                            Log.e(
//                                tag,
//                                "onResponse: result is: ${fromValueToDouble.times(result.asDouble)}",
//                                null
//                            )
//                            Log.e(
//                                tag,
//                                "onResponse: result is: ${
//                                    (BigDecimal(fromValueToDouble) * BigDecimal(result.asDouble)).setScale(
//                                        3,
//                                        BigDecimal.ROUND_HALF_UP
//                                    )
//                                }",
//                                null
//                            )
//
//                            toEditText.setText(
//                                String.format(
//                                    (BigDecimal(fromValueToDouble) * BigDecimal(result.asDouble)).setScale(
//                                        7,
//                                        BigDecimal.ROUND_HALF_UP
//                                    )
//                                        .toString()
//                                )
//                            )
//                        }
//
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        Log.d(tag, e.localizedMessage!!)
//                    }
//
//                }
//
//            }
//
//            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//
//                call.cancel()
//
//                Log.e(tag, "onFailure: It failed!", t)
//
//            }
//
//        })
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
        detailsTextView = findViewById(R.id.conversion_details_textView)
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