package com.example.quantumgame.ui.main

class Game {
    private var activePlayer: Player = Player()
    private var player1: Player = Player()

    private var player2: Player = Player()

    fun getActivePlayerName():String {
        return activePlayer.getName()
    }

    fun getActivePlayerCircuit():String{
        return activePlayer.circuit.getCircuitString()
    }

    fun startNewGame(){
        //player1 = Player("player1")
        //player2 = Player("player2")
        player1.playerName = "player1"
        player2.playerName = "player2"
        activePlayer = player1
    }

    fun playRound1Player(player:Player){
        player.circuit.addGateCombo(waitForTurn())
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
}