package com.example.quantumgame.ui.main

import android.os.AsyncTask
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
//import jdk.nashorn.internal.runtime.ScriptingFunctions.readLine
import android.system.Os.socket
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader



internal class RetrieveAnswerFromQuantumComputerTask(val thecallback: (answerStr:String) -> Unit) : AsyncTask<String, String, String>() {

    private var exception: Exception? = null

    override fun doInBackground(vararg params: String):String {
        val client = Socket("192.168.43.135", 65432)
        var str:String = params[0]
        client.outputStream.write(str.toByteArray(Charset.defaultCharset()))

        val inbuf = BufferedReader(
            InputStreamReader(client.getInputStream())
        )
        var fromServer = ""
        Log.e("task", "before readline")
        System.out.println("before readline")

        //fromServer = inbuf.readLine()

        /*while(true){
            fromServer = inbuf.readLine()
            if (fromServer != ""){
                Log.e("task", "inbuf is not null")
                System.out.println("not null")
                break
            } else{
                Log.e("task", "inbuf is null")
                System.out.println("not null")
            }
       }*/

        Log.e("task", "Finishing the task")
        System.out.println("finishing task")

        client.close()

        fromServer ="1258745874 4444444444"

        return fromServer
    }




    // from here: https://stackoverflow.com/questions/56535473/how-to-send-and-receive-strings-through-tcp-connection-using-kotlin
   /* fun retrieveAnswer():String {
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
    }*/

    override fun onPostExecute(str: String) {
        thecallback(str)
    }
}