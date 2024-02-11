package com.maxpayneman.sorteioparanaclinicas.Model.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.maxpayneman.sorteioparanaclinicas.Model.Colaborador
import com.maxpayneman.sorteioparanaclinicas.R
import com.maxpayneman.sorteioparanaclinicas.ViewModel.ColaboradorViewModel

class CostumeAdapter(context: Context,
                     private var listaDeItens: ArrayList<Colaborador>,
                     private val viewModel: ColaboradorViewModel) : ArrayAdapter<Colaborador>(context,
    R.layout.lista_item, listaDeItens) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.lista_item, parent, false)
        val viewHolder = ViewHolder(itemView)

        val item = listaDeItens[position]

        viewHolder.textViewItem.text = item.toString()

        viewHolder.removeButton.setOnClickListener {
            val idColaborador = item.selecionado.idColaborador
            if (idColaborador.isNotEmpty()) { // Verificar se o ID não está vazio
                viewModel.deletarColaborador(idColaborador)
                val novalista = listaDeItens.toMutableList()
                novalista.remove(item)
                updateLista(novalista)

            } else {
                Toast.makeText(context, "ID do ${item.selecionado.nome} não encontrado", Toast.LENGTH_SHORT).show()
            }
        }

        return itemView
    }

    private class ViewHolder(itemView: View) {
        val textViewItem: TextView = itemView.findViewById(R.id.text_view_item)
        val removeButton: Button = itemView.findViewById(R.id.remove)
    }

    fun updateLista(novaLista: MutableList<Colaborador>) {
        listaDeItens.clear()
        listaDeItens.addAll(novaLista)
        notifyDataSetChanged()
    }
}