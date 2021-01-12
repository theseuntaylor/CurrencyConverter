package com.theseuntaylor.currencyconverter.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.theseuntaylor.currencyconverter.R

class MainActivity : AppCompatActivity() {

    val TAG: String = "MainActivity TAG"

    private lateinit var toSpinner: Spinner
    private lateinit var toTextView: TextView

    private lateinit var fromSpinner: Spinner
    private lateinit var fromTextView: TextView

    private lateinit var swapButton: ImageButton
    private lateinit var convertButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViews()
        setUpSpinners()
        setViews()
        swapViews(swapButton, fromSpinner, toSpinner)

        convertButton.setOnClickListener {
            Log.i(TAG, "onCreate: button pressed")
        }
    }

    private fun findViews() {
        convertButton = findViewById(R.id.convert_button)
        toSpinner = findViewById(R.id.toSpinner)
        toTextView = findViewById(R.id.currency_to_textView)
        fromSpinner = findViewById(R.id.fromSpinner)
        fromTextView = findViewById(R.id.currency_from_textView)
        swapButton = findViewById(R.id.swap_spinners_button)
    }

    private fun setUpSpinners() {

        ArrayAdapter.createFromResource(
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

                Log.e(TAG, "onItemSelected: item selected is $text", null)
                Log.e(
                    TAG,
                    "onItemSelected: item selected number is ${parent?.getPositionForView(view)}",
                    null
                )
            }
        }
    }

    private fun swapViews(swapButtons: ImageButton, fromSpinner: Spinner, toSpinner: Spinner) {

        swapButtons.setOnClickListener {
            val item1: Int = fromSpinner.selectedItemPosition
            val item2: Int = toSpinner.selectedItemPosition

            toSpinner.setSelection(item1)

            fromSpinner.setSelection(item2)

        }
    }

}