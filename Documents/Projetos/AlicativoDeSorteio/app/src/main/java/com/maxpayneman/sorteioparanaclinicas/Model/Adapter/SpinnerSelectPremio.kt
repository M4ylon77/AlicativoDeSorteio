package com.maxpayneman.sorteioparanaclinicas.Model.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.maxpayneman.sorteioparanaclinicas.Model.Premio
import com.maxpayneman.sorteioparanaclinicas.R

class SpinnerSelectPremio(context: Context, private val items: List<Premio>) :
        ArrayAdapter<Premio>(context, R.layout.select_item, items) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return createViewFromResource(position, convertView, parent)
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return createViewFromResource(position, convertView, parent)
        }

        private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.select_item, parent, false)
            val textView = view.findViewById<TextView>(R.id.text1)
            textView.text = items[position].nome

            // Personalize as cores do texto aqui
            textView.setTextColor(context.resources.getColor(R.color.white))

            return view
        }
    }

