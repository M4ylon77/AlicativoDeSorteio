package com.maxpayneman.teamsorteio.Model

class Sorteio(var colaborador: Colaborador,var premio: Premio ) {

    override fun toString(): String {
        return "Nome: ${colaborador.selecionado.nome} Cracha: ${colaborador.selecionado.cracha} Ganhaou: ${premio.nome}"
    }
}