package com.example.quantumgame.ui.main

class Gate(nr:Int) {
    var gateStr: String = ""
    var type = nr

    fun getGateString(): String{
        var tmpStr: String = ""

        when (type) {
            0 -> gateStr = "i"
            1 -> gateStr = "a"
            2 -> gateStr = "h"
            3 -> gateStr = "b"
            else -> { // Note the block
                print("Gate does not exist")
            }
        }

        return tmpStr
    }

}