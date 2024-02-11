package com.maxpayneman.sorteioparanaclinicas.Model

class Colaborador(var selecionado: Selecionado) {
    val center = "                    "
    val centerEnd = "         "

    override fun toString(): String {
        return "${selecionado.nome} || ${selecionado.turno}"
    }
}