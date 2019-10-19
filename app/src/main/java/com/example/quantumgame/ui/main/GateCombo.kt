package com.example.quantumgame.ui.main

class GateCombo (nr1:Int, nr2:Int, nr3:Int){
    var gate1:Gate = Gate(nr1)
    var gate2:Gate = Gate(nr2)
    var gate3:Gate = Gate(nr3)

    fun getGateComboString(): String{
        return gate1.getGateString() + gate2.getGateString() + gate3.getGateString()
    }


}