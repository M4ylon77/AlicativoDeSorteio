package com.maxpayneman.sorteioparanaclinicas.ViewModel

import android.util.Log
import androidx.constraintlayout.widget.StateSet
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.maxpayneman.sorteioparanaclinicas.Model.Premio

class PremioViewModel : ViewModel() {

    private val _minhaLista = MutableLiveData<MutableList<Premio>>()
    private val _minhaLitaSelecionada = MutableLiveData<MutableList<Premio>>()
    private val db = FirebaseFirestore.getInstance()
    val minhaLista: LiveData<MutableList<Premio>> get() = _minhaLista
    val minhaListaSelecionada: LiveData<MutableList<Premio>> get() = _minhaLitaSelecionada
    private val premioSelecionado = db.collection("Premio")
    private val premioAddSelecionado = db.collection("PremioSelecionado")
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage


    private fun definirMensagem(mensagem: String) {
        _toastMessage.value = mensagem
    }
    init {
      listarPremio()
        listarPremioSelecionado()
    }

    fun addPremio(premio: Premio) {
        val nome = premio.nome.toString()


        val selecionadoMap = hashMapOf(
            "nome" to nome,
            "idPremio" to premioSelecionado.document().id

        )


        premioSelecionado.add(selecionadoMap)
            .addOnSuccessListener { documentReference ->
                val novoId = documentReference.id
                premioSelecionado.document(novoId)
                    .update("idPremio", novoId)
                    .addOnSuccessListener {
                        definirMensagem("Premio $novoId Adicionado com sucesso")
                        Log.d(TAG, "DocumentSnapshot added with ID: $novoId")
                        listarPremio()
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error updating document: ${e.message}", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding document: ${e.message}", e)
            }
    }

    fun selecionarPremio(premio: Premio) {
        val nome = premio.nome.toString()


        val selecionadoMap = hashMapOf(
            "nome" to nome,
            "idPremio" to premioAddSelecionado.document().id

        )


        premioAddSelecionado.add(selecionadoMap)
            .addOnSuccessListener { documentReference ->
                val novoId = documentReference.id
                premioAddSelecionado.document(novoId)
                    .update("idPremio", novoId)
                    .addOnSuccessListener {
                        definirMensagem("Premio $novoId Adicionado com sucesso")
                        Log.d(TAG, "DocumentSnapshot added with ID: $novoId")
                        listarPremioSelecionado()
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error updating document: ${e.message}", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding document: ${e.message}", e)
            }
    }




    fun listarPremio() {

        premioSelecionado.get()
            .addOnSuccessListener { documents ->
                val premio = mutableListOf<Premio>()
                for (document in documents) {
                    val nome = document.getString("nome")
                    val idPremio = document.getString("idPremio").toString()
                    if (nome != null ) {
                        val premios = Premio(nome, idPremio)
                        premio.add(premios)
                    }
                    _minhaLista.postValue(premio)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(StateSet.TAG, "Erro ao obter documentos: ", exception)
            }
    }


    fun listarPremioSelecionado() {

        premioAddSelecionado.get()
            .addOnSuccessListener { documents ->
                val premio = mutableListOf<Premio>()
                for (document in documents) {
                    val nome = document.getString("nome")
                    val idPremio = document.getString("idPremio").toString()
                    if (nome != null ) {
                        val premios = Premio(nome, idPremio)
                        premio.add(premios)
                    }
                    _minhaLitaSelecionada.postValue(premio)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(StateSet.TAG, "Erro ao obter documentos: ", exception)
            }
    }

    companion object {
        const val TAG = "ColaboradorViewModel"
    }

    fun deletarPremio(idPremio: String) {

        premioAddSelecionado.document(idPremio)
            .delete()
            .addOnSuccessListener {
                // Colaborador deletado com sucesso
                definirMensagem("Premio deletado com sucesso!!")
                listarPremioSelecionado()
            }
            .addOnFailureListener { e ->
                // Falha ao deletar o colaborador
                definirMensagem("Erro ao deletar colaborador: $e")
            }
    }




}