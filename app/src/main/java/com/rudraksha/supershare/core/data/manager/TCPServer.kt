package com.rudraksha.supershare.core.data.manager

import java.io.*
import java.net.ServerSocket
import java.net.Socket
import kotlinx.coroutines.*
import kotlin.concurrent.thread

class TCPServer(private val port: Int = 8888) {

    private var serverSocket: ServerSocket? = null
    private val clientHandlers = mutableListOf<Job>()

    fun startServer(onClientConnected: (Socket) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            serverSocket = ServerSocket(port)
            while (true) {
                val clientSocket = serverSocket?.accept()
                clientSocket?.let {
                    onClientConnected(it)
                    val job = handleClient(it)
                    clientHandlers.add(job)
                }
            }
        }
    }

    private fun handleClient(clientSocket: Socket): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            try {
                val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
                val writer = BufferedWriter(OutputStreamWriter(clientSocket.getOutputStream()))
                var line: String? = null
                while (clientSocket.isConnected && reader.readLine().also { line = it } != null) {
                    println("Received: $line")
                    writer.write("Echo: $line\n")
                    writer.flush()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                clientSocket.close()
            }
        }
    }

    fun listenToClient(socket: Socket, onMessage: (String) -> Unit) {
        thread {
            try {
                val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                var line: String? = null
                while (socket.isConnected && reader.readLine().also { line = it } != null) {
                    onMessage(line ?: "")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun stopServer() {
        clientHandlers.forEach { it.cancel() }
        serverSocket?.close()
    }
}

