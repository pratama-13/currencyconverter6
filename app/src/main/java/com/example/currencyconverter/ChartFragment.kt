package com.example.currencyconverter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dewakoding.androidchartjs.util.ChartType
import com.example.currencyconverter.databinding.FragmentChartBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class ChartFragment : Fragment() {

        private var _binding: FragmentChartBinding? = null
        private val binding get() = _binding!!

        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View? {
                _binding = FragmentChartBinding.inflate(inflater, container, false)
                fetchHistoricalData() // Fetch historical data for chart
                return binding.root
        }

        override fun onDestroyView() {
                super.onDestroyView()
                _binding = null
        }

        private fun fetchHistoricalData() {
                val API_URL = "https://api.currencyfreaks.com/latest?apikey=a5ddf94800f54361a6d3ad1210b4658c"
                val desiredCurrencies = listOf("EUR", "USD", "VND", "AUD", "JPY")

                CoroutineScope(Dispatchers.IO).launch {
                        try {
                                val response = URL(API_URL).readText()
                                val jsonObject = JSONObject(response)
                                val rates = jsonObject.getJSONObject("rates")

                                val currencyLabels = mutableListOf<String>()
                                val currencyValues = mutableListOf<Int>()

                                val currencyData = StringBuilder()
                                for (currency in desiredCurrencies) {
                                        val rate = rates.getString(currency).toFloat().toInt() // Convert rate to Int
                                        currencyLabels.add(currency)
                                        currencyValues.add(rate)
                                        currencyData.append("$currency: $rate\n")
                                }

                                withContext(Dispatchers.Main) {
                                        binding.currencyData.text = currencyData.toString()

                                        // Set Line Chart Data using API data
                                        val chartType = ChartType.LINE
                                        binding.androidChart1.setChart(
                                                chartType,
                                                currencyLabels.toTypedArray(),
                                                currencyValues.toTypedArray(),
                                                "Currency Rates"
                                        )
                                }
                        } catch (e: Exception) {
                                e.printStackTrace()
                                withContext(Dispatchers.Main) {
                                        binding.currencyData.text = "Failed to fetch data"
                                }
                        }
                }
        }

        data class BubbleEntity(
                val x: Float,
                val y: Float,
                val z: Float
        )
}
