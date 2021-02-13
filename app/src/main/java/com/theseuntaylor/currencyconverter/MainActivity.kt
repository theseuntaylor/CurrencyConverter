package com.theseuntaylor.currencyconverter

//import com.theseuntaylor.currencyconverter.ViewModel.CurrencyCoverterVM
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.theseuntaylor.currencyconverter.Model.Constanst
import com.theseuntaylor.currencyconverter.Model.ConvertedExchange
import com.theseuntaylor.currencyconverter.Model.CurrencyLiveConversionResponse
import com.theseuntaylor.currencyconverter.Model.Result
import com.theseuntaylor.currencyconverter.NetworkService.ApiNetworkConnector
import com.theseuntaylor.currencyconverter.NetworkService.ApiService
import com.theseuntaylor.currencyconverter.NetworkService.CurrencyApiService
import com.theseuntaylor.currencyconverter.ViewModel.CurrencyCoverterVM
import com.theseuntaylor.currencyconverter.ViewModel.ViewModelFactory
import com.theseuntaylor.currencyconverter.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onActivityCreated, which we
     * do in this Fragment.
     */
    @Suppress("DEPRECATION")
    private val viewModel: CurrencyCoverterVM by lazy {
        val activity = requireNotNull(this) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, ViewModelFactory(activity.application))
            .get(CurrencyCoverterVM::class.java)

    }

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toSpinner =
            binding.toSpinner       // Creates the spinner of the available currency you can convert to

        val fromSpinner =
            binding.fromSpinner   //Creates the spinner of the current currency we have available.

        val resultTextView = binding.currencyToEditText //creates the textview to populate result

        val fromEditText = binding.currencyFromEditText //cteates the edittext to get amount from

        val submitButton = binding.convertButton

        val clearButton = binding.clearButton

        val currencyToTextView = binding.currencyToTextView

        val currencyFromTextView = binding.currencyFromTextView

        ///This creates the spinners and populates them
        ArrayAdapter.createFromResource(
            this,
            R.array.from_currencies,
            android.R.layout.simple_dropdown_item_1line
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            toSpinner.adapter = adapter
            fromSpinner.adapter = adapter
        }

        toSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                val text: String = parent.getItemAtPosition(pos).toString()
                currencyToTextView.text = text
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        fromSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                val text: String = parent.getItemAtPosition(pos).toString()
                currencyFromTextView.text = text
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        //This is to observe the submission Livedata variable and then update the UI.
        viewModel.mCurrencyConverterResponse.observe(this, { type ->
            run {
                coroutineScope.launch {
                    when (type) {
                        is Result.Success -> {
                            resultTextView.text = NumberFormat.getNumberInstance(Locale.US).format((Math.round(type.response * 100.0) / 100.0))
                        }
                        is Result.NetworkError -> {
                            Snackbar.make(
                                binding.root,
                                "Check your Network Connection",
                                Snackbar.LENGTH_SHORT
                            )
                                .setAnimationMode(Snackbar.ANIMATION_MODE_FADE).show()
                        }
                        is Result.Failure -> {
                            Timber.tag("Failure").d(type.response.toString())
                            Snackbar.make(
                                binding.root,
                                type.response.toString(),
                                Snackbar.LENGTH_SHORT
                            )
                                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
                        }
                        is Result.Exception -> {
                            Timber.tag("Exception").d(type.t)
                            type.t.message?.let {
                                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                                    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
                            }
                        }
                    }
                }
                submitButton.revertAnimation()
            }

        })

        clearButton.setOnClickListener{
            fromEditText.text = null
            resultTextView.text = null

        }

        submitButton.setOnClickListener {
            submitButton.startAnimation()
            val amountString = fromEditText.text.trim().toString()
            val fromCurrency = fromSpinner.selectedItem.toString()
            val toCurrency = toSpinner.selectedItem.toString()

            Timber.tag("Amount").d(amountString)

            if (amountString.isEmpty() || fromCurrency.isEmpty() || toCurrency.isEmpty()) {
                Snackbar.make(binding.root, "Empty details sent", Snackbar.LENGTH_SHORT)
                    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
                submitButton.revertAnimation()
            }
            else if(fromCurrency == toCurrency){
                resultTextView.text = amountString
                submitButton.revertAnimation()
            }
            else {
                val amount = amountString.toInt()
                val currencies = fromCurrency + "_$toCurrency"
                Timber.tag("Currencies").d(currencies)
                viewModel.convertAmount(currencies, amount)
        }
            }


    }


    override fun onStop() {
        super.onStop()
        viewModelStore.clear()
    }
}