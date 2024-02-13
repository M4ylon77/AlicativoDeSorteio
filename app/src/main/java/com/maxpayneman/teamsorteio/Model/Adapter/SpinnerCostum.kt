package com.maxpayneman.teamsorteio.Model.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.maxpayneman.teamsorteio.R

class SpinnerCostum(context: Context, private val items: List<String>) :
        ArrayAdapter<String>(context, R.layout.select_item, items) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return createViewFromResource(position, convertView, parent)
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return createViewFromResource(position, convertView, parent)
        }

        private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.select_item, parent, false)
            val textView = view.findViewById<TextView>(R.id.text1)
            textView.text = items[position]

            // Personalize as cores do texto aqui
            textView.setTextColor(context.resources.getColor(R.color.white))

            return view
        }
    }
