package com.example.quantumgame.ui.main

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.quantumgame.R
import java.io.BufferedReader
import java.io.InputStreamReader
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
        // get reference to button
        val marketClick = view.findViewById(R.id.market) as LinearLayout
        // set on-click listener
        marketClick.setOnClickListener {
            Toast.makeText(context, getResultFromQuantumComputer(), Toast.LENGTH_SHORT).show()
        }

    }

    private var activePlayer: Player = Player()
    private var player1: Player = Player()

    private var player2: Player = Player()

    var randomGatesList:  ArrayList<ArrayList<Int>> =  ArrayList<ArrayList<Int>>()
    var goal: Goal = Goal(3)
    var market: ArrayList<GateCombo> = ArrayList<GateCombo>()
    var marketSize: Int = 3
    var roundsTotal: Double = 4.0
    var roundsCurrent: Double = 0.0

    private var qtask:RetrieveAnswerFromQuantumComputerTask = RetrieveAnswerFromQuantumComputerTask(::thetaskcallback)

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
        for (i in 0..(marketSize-1)){
            market.add(addNewGateComboToMarket())
        }
        goal = Goal(3)

        //now lets make a fake random goal that is always 4
        goal.goalColor= QuantumColor(4)
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
        val allPacks = ArrayList<ConstraintLayout>()
        allPacks.add(view.findViewById<ConstraintLayout>(R.id.pack1))
        val pack2 = view.findViewById<ConstraintLayout>(R.id.pack2)
        val pack3 = view.findViewById<ConstraintLayout>(R.id.pack3)
        for (i in 0..(market.size-1)) {
            //pack1.background = resources.getDrawable(grabCorrectIcon(market[i].gate1.type))
        }
    }

    fun grabCorrectIcon(gateType: Int): Int {
        when (gateType) {
            0 -> return R.drawable.ic_circle
            1 -> return R.drawable.ic_up_triangle
            2 -> return R.drawable.ic_rectangle
        }
        return R.drawable.ic_rectangle
    }

    fun createRandomSet(numberOfGates: Int, numberInPack: Int): ArrayList<Int> {
        val list = ArrayList<Int>()
        val finalList = ArrayList<Int>()
        for (i in 0..(numberOfGates-1)) {
            list.add(i)
        }
        Collections.shuffle(list)
        for (i in 0..(numberInPack-1)) {
            finalList.add(list[i])
        }
        return finalList
    }

    fun createAllPacks(numberOfGates: Int, numberInPack: Int): ArrayList<ArrayList<Int>> {
        var list = ArrayList<ArrayList<Int>>()
        var numberOfPacks:Double = (numberOfGates.toDouble()).pow(numberInPack) //nr in pack is nr of qbits
        for (i in 0..(numberOfPacks.toInt()-1)) {
            list.add(createRandomSet(numberOfGates, numberInPack))
        }
        return list
    }

    fun getResultFromQuantumComputer():String{

        var outStr = player1.circuit.getCircuitString() + " " + player2.circuit.getCircuitString()

        if (outStr.length != 25){
            outStr = "aaabbbhhhiii aaabbbhhhiii" // send something if we dont manage to play correctly
        }


        qtask.execute(outStr)

        return ""
    }

    fun thetaskcallback(answerstr:String){


        System.out.println(answerstr+ "!!!!!!!!!!!!!!!!!!!!!!!!!!!")

        var winner = findWinner(answerstr)

        Log.e("winner", winner+ "!!!!!!!!!!!!!!!!!!!!!!!!!!!")

        //TODO: call the last screen here

    }


    // from here: https://stackoverflow.com/questions/56535473/how-to-send-and-receive-strings-through-tcp-connection-using-kotlin
    fun retrieveAnswer():String {
        //var answers:ArrayList<String> = ArrayList<String>()
        var answer:String = ""
        val server = ServerSocket(65432)
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
        for (i in 0..(answer.length-1)){
            if (answer.get(i).toString().equals(goal.goalColor.getID().toString())){
                player.points++
                Log.e("points",player.points.toString())
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

        Log.e("player1 answer", answerPlayer1)
        Log.e("player2 answer", answerPlayer2)

        updatePlayerResult(player1, answerPlayer1)
        updatePlayerResult(player2, answerPlayer2)

        Log.e("player1 points", player1.points.toString())
        Log.e("player2 points", player2.points.toString())

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