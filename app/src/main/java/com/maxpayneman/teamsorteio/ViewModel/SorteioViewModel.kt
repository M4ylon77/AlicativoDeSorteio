package com.maxpayneman.teamsorteio.ViewModel

import android.util.Log
import androidx.constraintlayout.widget.StateSet
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.maxpayneman.teamsorteio.Model.Colaborador
import com.maxpayneman.teamsorteio.Model.Premio
import com.maxpayneman.teamsorteio.Model.Selecionado
import com.maxpayneman.teamsorteio.Model.Sorteio

class SorteioViewModel : ViewModel() {

    private val _minhaLista = MutableLiveData<MutableList<Sorteio>>()
    private val db = FirebaseFirestore.getInstance()
    val minhaLista: LiveData<MutableList<Sorteio>> get() = _minhaLista
    private val sorteioRealizado = db.collection("Sorteio")
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

init {
    listarSorteio()
}


    fun addSorteio(sorteio: Sorteio) {
        val nome = sorteio.colaborador.selecionado.nome.toString()
        val cracha = sorteio.colaborador.selecionado.cracha
        val idColaborador = sorteio.colaborador.selecionado.idColaborador
        val turno = sorteio.colaborador.selecionado.turno
        val premio = sorteio.premio.nome
        val idPremio = sorteio.premio.idPremio

        sorteioRealizado.whereEqualTo("cracha", cracha ).get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Nenhum documento encontrado com o mesmo número de crachá, pode salvar

                    val selecionadoMap = hashMapOf(
                        "nome" to nome,
                        "cracha" to cracha,
                        "idColaborador" to idColaborador,
                        "turno" to turno,
                        "premio" to premio,
                        "idPremio" to idPremio,
                        "idSorteio" to sorteioRealizado.document().id

                    )

                    sorteioRealizado.add(selecionadoMap)
                        .addOnSuccessListener { documentReference ->
                            val novoId = documentReference.id
                            sorteioRealizado.document(novoId)
                                .update("idColaborador", novoId)
                                .addOnSuccessListener {
                                    listarSorteio()
                                }
                                .addOnFailureListener { e ->
                                    Log.e(
                                        ColaboradorViewModel.toString(),
                                        "Error updating document: ${e.message}",
                                        e
                                    )
                                }
                        }
                        .addOnFailureListener { e ->
                            Log.e(
                                ColaboradorViewModel.toString(),
                                "Error adding document: ${e.message}",
                                e
                            )
                        }
                } else {
                    Log.e(
                        ColaboradorViewModel.toString(),
                        "Já existe um colaborador com o mesmo número de crachá"
                    )
                }

            }
    }

    fun listarSorteio() {

        sorteioRealizado.get()
            .addOnSuccessListener { documents ->
                val sorteio = mutableListOf<Sorteio>()
                for (document in documents) {
                    val nome = document.getString("nome")
                    val cracha = document.getString("cracha").toString()
                    val turno = document.getString("turno").toString()
                    val idColaborador = document.getString("idColaborador").toString()
                    val premio = document.getString("premio").toString()
                    val idPremio = document.getString("idPremio").toString()
                    if (nome != null ) {
                        var colaborador = Colaborador(Selecionado(nome,turno,idColaborador,cracha))
                        var premio = Premio(premio,idPremio)
                        val sorteios = Sorteio(colaborador,premio)
                        sorteio.add(sorteios)
                    }
                    _minhaLista.postValue(sorteio)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(StateSet.TAG, "Erro ao obter documentos: ", exception)
            }
    }



    fun deletarlistadeSorteados(idSorteio: String) {

        sorteioRealizado.document(idSorteio)
            .delete()
            .addOnSuccessListener {
                listarSorteio()
            }
            .addOnFailureListener { e ->
                // Falha ao deletar o colaborador
            }
    }


}