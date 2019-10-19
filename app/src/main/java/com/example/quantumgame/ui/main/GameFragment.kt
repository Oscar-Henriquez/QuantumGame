package com.example.quantumgame.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.quantumgame.R
import java.util.*
import kotlin.collections.ArrayList


class GameFragment : Fragment() {

    companion object {
        fun newInstance() = GameFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.game_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    fun createRandomSet(numberOfGates: Int, numberInPack: Int): ArrayList<Int> {
        val list = ArrayList<Int>()
        val finalList = ArrayList<Int>()
        for (i in 1..numberOfGates) {
            list.add(i)
        }
        Collections.shuffle(list)
        for (i in 0..numberInPack) {
            finalList[i] =  list[i]
        }
        return finalList
    }

    fun createAllPacks(numberOfPacks: Int, numberOfGates: Int, numberInPack: Int) {
        ArrayList<ArrayList<Int>>()
        for (i in 0..numberOfPacks) {
            createRandomSet(numberOfGates, numberInPack)
        }
    }


}