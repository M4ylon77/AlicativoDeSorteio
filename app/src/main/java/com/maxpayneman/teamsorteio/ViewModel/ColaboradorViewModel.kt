package com.maxpayneman.teamsorteio.ViewModel

import android.util.Log
import androidx.constraintlayout.widget.StateSet
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.maxpayneman.teamsorteio.Model.Colaborador
import com.maxpayneman.teamsorteio.Model.Selecionado

class ColaboradorViewModel : ViewModel() {

    private val _minhaLista = MutableLiveData<MutableList<Colaborador>>()
    private val _minhaListaSelecionados = MutableLiveData<MutableList<Selecionado>>()
    private val db = FirebaseFirestore.getInstance()
    val minhaLista: LiveData<MutableList<Colaborador>> get() = _minhaLista
    val minhaListaSelecionado: LiveData<MutableList<Selecionado>>get() = _minhaListaSelecionados
    private val colaboradoresCollection = db.collection("colaboradores")
    private val colab = colaboradoresCollection.document().id
    private val selectColab = db.collection("selecionaveis")
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage


    private fun definirMensagem(mensagem: String) {
        _toastMessage.value = mensagem
    }
init {
    listarColaborador()
    listaSelecionaveis()
}

    fun addSelecter(selecionado: Selecionado) {
        val nome = selecionado.nome
        val turno = selecionado.turno
        val cracha = selecionado.cracha


        val selecionadoMap = hashMapOf(
            "nome" to nome,
            "turno" to turno,
            "cracha" to cracha,
            "idColaborador" to selectColab.document().id

        )


        selectColab.add(selecionadoMap)
            .addOnSuccessListener { documentReference ->
                val novoId = documentReference.id
                selectColab.document(novoId)
                    .update("idColaborador", novoId)
                    .addOnSuccessListener {
                        definirMensagem("Colaborador $novoId Adicionado com sucesso")
                        Log.d(TAG, "DocumentSnapshot added with ID: $novoId")
                        listarColaborador()
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error updating document: ${e.message}", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding document: ${e.message}", e)
            }
    }


        fun cadastrarUsuario(colaborador: Colaborador) {
            val nome = colaborador.selecionado.nome
            val turno = colaborador.selecionado.turno
            val cracha = colaborador.selecionado.cracha

            colaboradoresCollection.whereEqualTo("cracha", cracha).get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        // Nenhum documento encontrado com o mesmo número de crachá, pode salvar
                        val colaboradorMap = hashMapOf(
                            "nome" to nome,
                            "turno" to turno,
                            "cracha" to cracha,
                            "idColaborador" to colaboradoresCollection.document().id
                        )

                        colaboradoresCollection.add(colaboradorMap)
                            .addOnSuccessListener { documentReference ->
                                val novoId = documentReference.id
                                colaboradoresCollection.document(novoId)
                                    .update("idColaborador", novoId)
                                    .addOnSuccessListener {
                                        definirMensagem("Colaborador $nome adicionado com sucesso")
                                        Log.d(TAG, "DocumentSnapshot added with ID: $novoId")
                                        listarColaborador()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e(TAG, "Error updating document: ${e.message}", e)
                                    }
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Error adding document: ${e.message}", e)
                            }
                    } else {
                        // Já existe um documento com o mesmo número de crachá, mostrar mensagem de erro
                        definirMensagem("Já existe um colaborador com o mesmo número de crachá")
                        Log.e(TAG, "Já existe um colaborador com o mesmo número de crachá")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error getting documents: ${e.message}", e)
                }
        }


fun listarColaborador() {

    colaboradoresCollection.get()
        .addOnSuccessListener { documents ->
            val colaboradores = mutableListOf<Colaborador>()
            for (document in documents) {
                val nome = document.getString("nome")
                val turno = document.getString("turno")
                val cracha = document.getString("cracha").toString()
                val idColaborador = document.getString("idColaborador").toString()
                if (nome != null && turno != null) {
                    val selecionado = Selecionado(nome, turno, idColaborador, cracha )
                    colaboradores.add(Colaborador(selecionado))
                }
                _minhaLista.postValue(colaboradores)
            }
        }
        .addOnFailureListener { exception ->
            Log.w(StateSet.TAG, "Erro ao obter documentos: ", exception)
        }
}
    fun listaSelecionaveis() {
        selectColab.get()
            .addOnSuccessListener { documents ->
                val listaSelecionados = mutableListOf<Selecionado>() // Crie uma lista para armazenar os objetos Selecionado
                for (document in documents) {
                    val nome = document.getString("nome")
                    val turno = document.getString("turno")
                    val cracha = document.getString("cracha").toString()
                    val idColaborador = document.getString("idColaborador").toString()
                    if (nome != null && turno != null) {
                       val selecionado = (Selecionado(nome, turno,idColaborador, cracha))
                        listaSelecionados.add(selecionado)

                    }
                    _minhaListaSelecionados.postValue(listaSelecionados)
                }
                // Chame setupRecyclerView após inicializar o adaptador
            }
            .addOnFailureListener { exception ->
                Log.w(StateSet.TAG, "Erro ao obter documentos: ", exception)
            }
    }



    companion object {
        private const val TAG = "ColaboradorViewModel"
    }

    fun deletarColaborador(idColaborador: String) {

        colaboradoresCollection.document(idColaborador)
            .delete()
            .addOnSuccessListener {
                // Colaborador deletado com sucesso
                definirMensagem("Colaborador deletado com sucesso!!")
                 listarColaborador()
            }
            .addOnFailureListener { e ->
                // Falha ao deletar o colaborador
                definirMensagem("Erro ao deletar colaborador: $e")
            }
    }



}