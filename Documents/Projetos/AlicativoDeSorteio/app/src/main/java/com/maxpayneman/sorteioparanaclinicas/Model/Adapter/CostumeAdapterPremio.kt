package com.maxpayneman.sorteioparanaclinicas.Model.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.maxpayneman.sorteioparanaclinicas.Model.Premio
import com.maxpayneman.sorteioparanaclinicas.R
import com.maxpayneman.sorteioparanaclinicas.ViewModel.PremioViewModel

class CostumeAdapterPremio(context: Context,
                           private var listaDePremios: ArrayList<Premio>,
                           private val viewModel: PremioViewModel) : ArrayAdapter<Premio>(context,
    R.layout.list_premio, listaDePremios) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_premio, parent, false)
        val viewHolder = ViewHolder(itemView)

        val item = listaDePremios[position]

        viewHolder.textViewItem.text = item.toString()

        viewHolder.removeButton.setOnClickListener {
            val idPremio = item.idPremio
            if (idPremio.isNotEmpty()) { // Verificar se o ID não está vazio
                viewModel.deletarPremio(idPremio)
                val novalista = listaDePremios.toMutableList()
                novalista.remove(item)
                updateLista(novalista)

            } else {
                Toast.makeText(context, "ID do ${item.nome} não encontrado", Toast.LENGTH_SHORT).show()
            }
        }

        return itemView
    }

    private class ViewHolder(itemView: View) {
        val textViewItem: TextView = itemView.findViewById(R.id.text_view_item)
        val removeButton: Button = itemView.findViewById(R.id.remove)
    }

    fun updateLista(novaLista: MutableList<Premio>) {
        listaDePremios.clear()
        listaDePremios.addAll(novaLista)
        notifyDataSetChanged()
    }
}