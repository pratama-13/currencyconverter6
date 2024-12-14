package com.example.currencyconverter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class ChartFragment : Fragment() {

    private lateinit var currencyTextView: TextView
    private lateinit var lineChart: LineChartView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chart, container, false)
        currencyTextView = view.findViewById(R.id.currency_data)
        lineChart = view.findViewById(R.id.line_chart)

        fetchHistoricalData() // Fetch historical data for chart
        return view
    }

    private fun fetchHistoricalData() {
        val API_URL = "https://api.currencyfreaks.com/latest?apikey=a5ddf94800f54361a6d3ad1210b4658c"
        val desiredCurrencies = listOf("EUR", "USD", "VND", "AUD", "JPY")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = URL(API_URL).readText()
                val jsonObject = JSONObject(response)
                val rates = jsonObject.getJSONObject("rates")

                val dataPoints = mutableListOf<Pair<Float, Float>>()
                val currencyData = StringBuilder()
                for ((index, currency) in desiredCurrencies.withIndex()) {
                    val rate = rates.getString(currency).toFloat()
                    dataPoints.add(rate to rate) // Use actual currency rate for both axes
                    currencyData.append("$currency: $rate\n")
                }

                withContext(Dispatchers.Main) {
                    currencyTextView.text = currencyData.toString()
                    lineChart.setData(dataPoints) // Update chart with historical data
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    currencyTextView.text = "Failed to fetch data"
                }
            }
        }
    }
}
