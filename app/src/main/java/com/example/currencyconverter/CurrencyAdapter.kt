package com.example.currencyconverter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class CurrencyAdapter(
    context: Context,
    private val currencies: List<String>,
    private val flags: List<String>,
    private val rates: List<String>
) : ArrayAdapter<String>(context, 0, currencies) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.currency_item, parent, false)

        val currencyName = view.findViewById<TextView>(R.id.currencyName)
        val currencyCode = view.findViewById<TextView>(R.id.currencyCode)
        val currencyRate = view.findViewById<TextView>(R.id.currencyRate)
        val flagImage = view.findViewById<ImageView>(R.id.flagImage)

        currencyName.text = currencies[position]
        currencyCode.text = "Code: ${currencies[position].substring(0, 3)}"
        currencyRate.text = rates[position]
        Glide.with(context).load(flags[position]).into(flagImage)

        return view
    }
}
