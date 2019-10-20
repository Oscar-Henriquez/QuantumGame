package com.example.quantumgame.ui.main

class Circuit {
    val appliedGates = mutableListOf<GateCombo>()

    fun addGateCombo(newGateCombo:GateCombo){
        appliedGates.add(newGateCombo)
    }

    fun getCircuitString(): String {
        var circuitString = ""
        for (gateC in appliedGates){
            circuitString += gateC.getGateComboString()
        }

        return circuitString
    }
}