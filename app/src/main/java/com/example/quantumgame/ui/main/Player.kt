package com.example.quantumgame.ui.main

class Player () {
    var playerName:String = ""

    // getter
    get() = field

    // setter
    set(value) {
        field = value
    }

    var circuit: Circuit = Circuit()
    // getter
    get() = field

    // setter
    set(value) {
        field = value
    }

    var points:Int = 0
    var colorResult:ArrayList<QuantumColor> = ArrayList<QuantumColor>()


    fun getName():String{
        return this.playerName
    }
}