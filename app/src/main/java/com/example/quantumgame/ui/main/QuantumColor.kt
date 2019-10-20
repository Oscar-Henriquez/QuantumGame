package com.example.quantumgame.ui.main

class QuantumColor(colId:Int = 0) {

    var colorID:Int = colId

    fun makeRandom(nrColors:Int){
        colorID = getRandomColor(nrColors)
    }

    fun getRandomColor(nrColors:Int):Int{
        var maxColNr = nrColors - 1
        return (0..maxColNr).random()
    }

    fun getID():Int{
        return colorID
    }
}