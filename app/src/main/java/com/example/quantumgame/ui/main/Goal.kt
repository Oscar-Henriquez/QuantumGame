package com.example.quantumgame.ui.main

import java.util.*
import kotlin.collections.ArrayList

class Goal(nrQbitsInit:Int = 3) {
    var nrQbits: Int = nrQbitsInit
    var goalColor: QuantumColor = generateGoal()

  /*  fun getAnswerStr():String{
        var answer:String = ""
        for (i in 0..nrQbits){
            answer += goalRGB[i].toString()
        }
        return answer
    }*/

    private fun generateGoal(): QuantumColor{
       var qc: QuantumColor = QuantumColor()
       qc.makeRandom(8)
        return qc
    }
}