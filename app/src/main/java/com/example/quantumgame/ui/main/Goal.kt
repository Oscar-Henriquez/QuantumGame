package com.example.quantumgame.ui.main

import java.util.*
import kotlin.collections.ArrayList

class Goal(nrQbitsInit:Int = 3) {
    var nrQbits: Int = nrQbitsInit
    var goalRGB: ArrayList<Int> = generateGoal()

    fun getAnswerStr():String{
        var answer:String = ""
        for (i in 0..nrQbits){
            answer += goalRGB[i].toString()
        }
        return answer
    }

    private fun generateGoal(): ArrayList<Int> {
        val list = ArrayList<Int>()

        for (i in 0..nrQbits) {
            list.add((0..1).random())
        }
        Collections.shuffle(list)

        return list
    }
}