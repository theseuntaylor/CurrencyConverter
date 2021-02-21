package com.theseuntaylor.currencyconverter.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.theseuntaylor.currencyconverter.Model.Result
import com.theseuntaylor.currencyconverter.R
import com.theseuntaylor.currencyconverter.ViewModel.CurrencyCoverterVM
import com.theseuntaylor.currencyconverter.ViewModel.ViewModelFactory
import com.theseuntaylor.currencyconverter.databinding.FragmentHomeBinding
import kotlinx.coroutines.*
import timber.log.Timber
import java.text.NumberFormat
import java.util.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @Suppress("DEPRECATION")
    private val viewModel: CurrencyCoverterVM by lazy {
        val activity = requireActivity()
        ViewModelProviders.of(this, ViewModelFactory(activity.application))
            .get(CurrencyCoverterVM::class.java)

    }

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHomeBinding.inflate(layoutInflater)

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
        viewModel.mCurrencyConverterResponse.observe(requireActivity(), { type ->
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

        return binding.root
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
    }
}