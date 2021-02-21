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


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }


    override fun onStop() {
        super.onStop()

    }
}