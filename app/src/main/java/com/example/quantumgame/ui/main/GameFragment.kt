package com.example.quantumgame.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.quantumgame.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.*


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

    private var activePlayer: Player = Player()
    private var player1: Player = Player()

    private var player2: Player = Player()

    var randomGatesList:  ArrayList<ArrayList<Int>> =  ArrayList<ArrayList<Int>>()
    var market: ArrayList<GateCombo> = ArrayList<GateCombo>();
    var marketSize: Int = 3;
    var roundsTotal: Double = 4.0;
    var roundsCurrent: Double = 0.0;

    fun getActivePlayerName():String {
        return activePlayer.getName()
    }

    fun getActivePlayerCircuit():String{
        return activePlayer.circuit.getCircuitString()
    }

    fun startNewGame(nrRounds: Double = 4.0){
        //player1 = Player("player1")
        //player2 = Player("player2")
        player1.playerName = "player1"
        player2.playerName = "player2"
        activePlayer = player1
        roundsTotal = nrRounds
        roundsCurrent = 0.0
        randomGatesList = createAllPacks(4,3) // from here we pick the gates for the market
        for (i in 0..marketSize){
            market.add(addNewGateComboToMarket())
        }

    }

    fun addNewGateComboToMarket():GateCombo{
        var newGate:ArrayList<Int> = randomGatesList.shuffled().take(1)[0]
        return GateCombo(newGate[0], newGate[1], newGate[2])
    }

    // returns true if game is finished
    fun move(selectedGateCombo:GateCombo):Boolean{
        //input: selected gate
        //add it to circuti
        activePlayer.circuit.addGateCombo(selectedGateCombo)
        roundsCurrent += 0.5
        if (roundsCurrent == roundsTotal){
            return true // time to show end screen and call evaluate result function
        } else {
            switchActivePlayer()
            return false
        }

    }

    fun switchActivePlayer(){
        if (activePlayer.equals(player1)){
            activePlayer = player2
        } else {
            activePlayer = player1
        }
    }

    fun playGame(nrRounds:Int = 3){
        var x = nrRounds
        while(x > 0){
            playRound1Player(player1)
            playRound1Player(player2)
            x--
        }
    }

    fun addSomeGatesToPlayer1Test(){
        var gate:Gate = Gate()
        gate.qbit1 = "H"
        gate.qbit2 = "0"
        gate.qbit3 = "0"
        player1.circuit.addGate(gate)
        gate = Gate()
        gate.qbit1 = "0"
        gate.qbit2 = "0"
        gate.qbit3 = "X"
        player1.circuit.addGate(gate)
        gate = Gate()
        gate.qbit1 = "0"
        gate.qbit2 = "0"
        gate.qbit3 = "Z"
        player1.circuit.addGate(gate)
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

    fun createAllPacks(numberOfGates: Int, numberInPack: Int): ArrayList<ArrayList<Int>> {
        var list = ArrayList<ArrayList<Int>>()
        var numberOfPacks:Double = (numberOfGates.toDouble()).pow(numberInPack) //nr in pack is nr of qbits
        for (i in 0..numberOfPacks.toInt()) {
            list.add(createRandomSet(numberOfGates, numberInPack))
        }
        return list
    }

    fun getResultFromQuantumComputer(){
        val client = Socket("192.168.43.49", 9999)
        client.outputStream.write("Hello from the client!".toByteArray())
        client.close()
    }

}