package com.example.quantumgame.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.quantumgame.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.*

import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset


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
        setUpGameUI(view)
    }

    private var activePlayer: Player = Player()
    private var player1: Player = Player()

    private var player2: Player = Player()

    var randomGatesList:  ArrayList<ArrayList<Int>> =  ArrayList<ArrayList<Int>>()
    var goal: Goal = Goal(3)
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
        goal = Goal(3)
    }

    fun addNewGateComboToMarket():GateCombo{
        var newGate:ArrayList<Int> = randomGatesList.shuffled().take(1)[0]
        return GateCombo(newGate[0], newGate[1], newGate[2])
    }

    // returns true if game is finished
    fun move(selectedGateCombo:GateCombo):Boolean{
        //input: selected gate
        //add it to circuti
        //market.remove the gate combo ... how to identify it
        var roundsCompleted: Boolean = false

        activePlayer.circuit.addGateCombo(selectedGateCombo)
        roundsCurrent += 0.5
        if (roundsCurrent < roundsTotal){
            market.remove(selectedGateCombo)
            addNewGateComboToMarket()
            switchActivePlayer()
        } else {
            roundsCompleted = true // time to show end screen and call evaluate result function
        }
        return roundsCompleted
    }

    fun switchActivePlayer(){
        if (activePlayer.equals(player1)){
            activePlayer = player2
        } else {
            activePlayer = player1
        }
    }



  /*  fun addSomeGatesToPlayer1Test(){
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
    }*/


    fun setUpGameUI(view: View) {

    }

    fun createRandomSet(numberOfGates: Int, numberInPack: Int): ArrayList<Int> {
        val list = ArrayList<Int>()
        val finalList = ArrayList<Int>()
        for (i in 0..numberOfGates) {
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

    fun getResultFromQuantumComputer():String{
        val client = Socket("192.168.43.49", 9999)
        var outStr = player1.circuit.getCircuitString() + " " + player2.circuit.getCircuitString()
        client.outputStream.write(outStr.toByteArray(Charset.defaultCharset()))
        client.close()

       return findWinner(retrieveAnswer())
    }


    // from here: https://stackoverflow.com/questions/56535473/how-to-send-and-receive-strings-through-tcp-connection-using-kotlin
    fun retrieveAnswer():String {
        //var answers:ArrayList<String> = ArrayList<String>()
        var answer:String = ""
        val server = ServerSocket(9999)
        println("Server running on port ${server.localPort}")
        val client = server.accept()
        println("Client connected : ${client.inetAddress.hostAddress}")
        val scanner = Scanner(client.inputStream)
        while (scanner.hasNextLine()) {
            answer = scanner.nextLine()
            break
        }
        server.close()
        return answer
    }

    fun updatePlayerResult(player:Player, answer:String){
        for (i in 0..answer.length){
            if (answer.get(i).toInt() == goal.goalColor.getID()){
                player.points++
                player.colorResult.add(QuantumColor(answer.get(i).toInt()))
            }
        }
    }

    // interpret the two received results
    // compare against the goal
    // return the winner
    fun findWinner(answer:String):String{
        val parts = answer.split(" ")
        var answerPlayer1 = parts[0]
        var answerPlayer2 = parts[1]

        var player1won: Boolean = false
        var player2won: Boolean = false
        var tie : Boolean = false
        var result:String = "tie"

        updatePlayerResult(player1, answerPlayer1)
        updatePlayerResult(player2, answerPlayer2)

        if (player1.points > player2.points){
            player1won = true
            result = "player1"
        } else if (player2.points < player1.points){
            player2won = true
            result = "player2"
        }else{
            tie = true
            result = "tie"
        }
        return result
    }

}