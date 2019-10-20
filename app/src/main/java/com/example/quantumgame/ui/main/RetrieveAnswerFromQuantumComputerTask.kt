package com.example.quantumgame.ui.main

import android.os.AsyncTask
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import jdk.nashorn.internal.runtime.ScriptingFunctions.readLine
import android.system.Os.socket
import java.io.BufferedReader
import java.io.InputStreamReader



internal class RetrieveAnswerFromQuantumComputerTask(val thecallback: (answerStr:String) -> Unit) : AsyncTask<String, String, String>() {

    private var exception: Exception? = null

    override fun doInBackground(vararg params: String):String {
        val client = Socket("192.168.43.135", 65432)

        client.outputStream.write(params.toString().toByteArray(Charset.defaultCharset()))

        val inbuf = BufferedReader(
            InputStreamReader(client.getInputStream())
        )
        val fromServer = inbuf.readLine()


        client.close()

        return fromServer
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

    override fun onPostExecute(str: String) {
        thecallback(str)
    }
}