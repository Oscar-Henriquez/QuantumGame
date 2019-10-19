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


    fun getName():String{
        return this.playerName
    }
}