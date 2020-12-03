package com.theseuntaylor.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toSpinner : Spinner = findViewById(R.id.toSpinner)
        val fromSpinner : Spinner = findViewById(R.id.fromSpinner)

        ArrayAdapter.createFromResource(
            this,
            R.array.from_currencies,
            android.R.layout.simple_dropdown_item_1line
        ).also {
            adapter -> adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            toSpinner.adapter = adapter
            fromSpinner.adapter = adapter
        }
    }
}