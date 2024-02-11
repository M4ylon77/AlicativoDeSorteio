package com.maxpayneman.sorteioparanaclinicas.Model

class Selecionado (var nome : String, var turno : String , var idColaborador: String, var cracha : String)  {

    override fun toString(): String {
        return "$nome || $cracha"
    }


}