package com.theseuntaylor.currencyconverter.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.theseuntaylor.currencyconverter.Model.ConversionResult
import com.theseuntaylor.currencyconverter.Model.Result
import com.theseuntaylor.currencyconverter.Model.SymbolResult
import com.theseuntaylor.currencyconverter.R
import com.theseuntaylor.currencyconverter.ViewModel.CurrencyCoverterVM
import com.theseuntaylor.currencyconverter.ViewModel.ViewModelFactory
import com.theseuntaylor.currencyconverter.databinding.FragmentHomeBinding
import kotlinx.coroutines.*
import timber.log.Timber
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var toSpinner: Spinner

    private lateinit var fromSpinner: Spinner


    @Suppress("DEPRECATION")
    private val viewModel: CurrencyCoverterVM by lazy {
        val activity = requireActivity()
        ViewModelProvider(this, ViewModelFactory(activity.application))
            .get(CurrencyCoverterVM::class.java)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        viewModel.getSymbol()
    }


    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHomeBinding.inflate(layoutInflater)


        toSpinner =
            binding.toSpinner       // Creates the spinner of the available currency you can convert to

        fromSpinner =
            binding.fromSpinner   //Creates the spinner of the current currency we have available.

        val resultTextView = binding.currencyToEditText //creates the textview to populate result

        val resultRateTextView = binding.rateResultView //creates the textview to populate result

        val fromEditText = binding.currencyFromEditText //cteates the edittext to get amount from

        val submitButton = binding.convertButton

        val clearButton = binding.clearButton

        val currencyToTextView = binding.currencyToTextView

        val currencyFromTextView = binding.currencyFromTextView

        val swapButton = binding.swapButton

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

        /* //This is to observe the submission Livedata variable and then update the UI.
         viewModel.mCurrencyConverterResponse.observe(requireActivity(), { type ->
             run {
                 coroutineScope.launch {
                     if (!type.hasBeenHandled) {
                         when (val result = type.getContentIfNotHandled()) {
                             is Result.Success -> {
                                 resultTextView.text = NumberFormat.getNumberInstance(Locale.US)
                                     .format((Math.round(result.response * 100.0) / 100.0))
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
                                 Timber.tag("Failure").d(result.response.toString())
                                 Snackbar.make(
                                     binding.root,
                                     result.response.toString(),
                                     Snackbar.LENGTH_SHORT
                                 )
                                     .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
                             }
                             is Result.Exception -> {
                                 Timber.tag("Exception").d(result.t)
                                 result.t.message?.let {
                                     Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                                         .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
                                 }
                             }
                         }
                     }
                 }
                 submitButton.revertAnimation()
                 swapButton.revertAnimation()
             }

         })*/

        //This is to observe the submission Livedata variable and then update the UI.
        viewModel.mCurrencyConversionResponse.observe(requireActivity(), { type ->
            run {
                coroutineScope.launch {
                    withContext(Dispatchers.Main) {
                        if (!type.hasBeenHandled) {
                            when (val result = type.getContentIfNotHandled()) {
                                is ConversionResult.Success -> {
                                    val fromCurrency = fromSpinner.selectedItem.toString()
                                    val toCurrency = toSpinner.selectedItem.toString()
                                    resultTextView.text = result.response.result.toString()
                                    resultRateTextView.text = String.format(
                                        getString(R.string.rate_result),
                                        result.response.info.rate, fromCurrency, toCurrency
                                    )
                                }
                                is ConversionResult.NetworkError -> {
                                    Snackbar.make(
                                        binding.root,
                                        "Check your Network Connection",
                                        Snackbar.LENGTH_SHORT
                                    )
                                        .setAnimationMode(Snackbar.ANIMATION_MODE_FADE).show()
                                }
                                is ConversionResult.Failure -> {
                                    Timber.tag("Failure").d(result.response.toString())
                                    Snackbar.make(
                                        binding.root,
                                        result.response.toString(),
                                        Snackbar.LENGTH_SHORT
                                    )
                                        .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
                                }
                                is ConversionResult.Exception -> {
                                    Timber.tag("Exception").d(result.t)
                                    result.t.message?.let {
                                        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                                            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
                                    }
                                }
                            }
                        }
                    }
                }
                submitButton.revertAnimation()
                swapButton.revertAnimation()
            }

        })

        clearButton.setOnClickListener {
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
            } else if (fromCurrency == toCurrency) {
                resultTextView.text = amountString
                submitButton.revertAnimation()
            } else {
                val amount = amountString.toInt()
                val currencies = fromCurrency + "_$toCurrency"
                Timber.tag("Currencies").d(currencies)
                viewModel.convertAmountAndGetRate(fromCurrency, toCurrency, amount)
                //viewModel.convertAmount(currencies, amount)
            }
        }

        swapButton.setOnClickListener {
            swapButton.startAnimation()
            val amountString = fromEditText.text.trim().toString()
            val fromCurrency = fromSpinner.selectedItem.toString()
            val fromCurrencySpinnerPosition = fromSpinner.selectedItemPosition
            val toCurrency = toSpinner.selectedItem.toString()
            val toCurrencySpinnerPosition = toSpinner.selectedItemPosition

            fromSpinner.setSelection(toCurrencySpinnerPosition, true)
            toSpinner.setSelection(fromCurrencySpinnerPosition, true)



            Timber.tag("Amount").d(amountString)

            if (amountString.isEmpty() || fromCurrency.isEmpty() || toCurrency.isEmpty()) {
                Snackbar.make(binding.root, "Empty details sent", Snackbar.LENGTH_SHORT)
                    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
                swapButton.revertAnimation()
            } else if (fromCurrency == toCurrency) {
                resultTextView.text = amountString
                swapButton.revertAnimation()
            } else {
                val amount = amountString.toInt()
                val currencies = toCurrency + "_$fromCurrency"
                Timber.tag("Currencies").d(currencies)
                //viewModel.convertAmount(currencies, amount)
                viewModel.convertAmountAndGetRate(fromCurrency, toCurrency, amount)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //This is to observe the submission Livedata variable and then update the UI.
        viewModel.mCurrencySymbolResponse.observe(requireActivity(), { type ->
            run {
                coroutineScope.launch {
                    withContext(Dispatchers.Main) {
                        if (!type.hasBeenHandled) {
                            when (val result = type.getContentIfNotHandled()) {
                                is SymbolResult.Success -> {
                                    if (!result.response.symbols.isNullOrEmpty()
                                        && result.response.symbols.isNotEmpty()
                                    ) {
                                        ///This creates the spinners and populates them\
                                        val spinnerDataList: ArrayList<String> = ArrayList()
                                        val spinnerDataArray: Array<String>
                                        for (eachData in result.response.symbols) {
                                            spinnerDataList.add(eachData.key)
                                        }
                                        spinnerDataArray = spinnerDataList.toTypedArray()
                                        activity?.let {
                                            ArrayAdapter(
                                                it.baseContext,
                                                android.R.layout.simple_dropdown_item_1line,
                                                spinnerDataArray
                                            ).also { adapter ->
                                                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                                                toSpinner.adapter = adapter
                                                fromSpinner.adapter = adapter
                                            }
                                        }
                                    } else {
                                        loadStaticSpinner()
                                    }
                                }
                                is SymbolResult.NetworkError -> {
                                    loadStaticSpinner()
                                }
                                is SymbolResult.Failure -> {
                                    loadStaticSpinner()
                                }
                                is SymbolResult.Exception -> {
                                    loadStaticSpinner()
                                }
                            }
                        }
                    }
                }
            }
        })


    }

    private fun loadStaticSpinner() {
        ///This creates the spinners and populates them
        activity?.let {
            ArrayAdapter.createFromResource(
                it.baseContext,
                R.array.from_currencies,
                android.R.layout.simple_dropdown_item_1line
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                toSpinner.adapter = adapter
                fromSpinner.adapter = adapter
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    override fun onStop() {
        super.onStop()
        viewModelStore.clear()
        coroutineScope.cancel()
    }
}