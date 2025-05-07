package com.rudraksha.supershare.core.data.manager

import android.util.Log
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import kotlin.concurrent.thread

class TCPClient(private val serverIp: String, private val serverPort: Int = 8888) {

    private var socket: Socket? = null
    private var writer: PrintWriter? = null
    private var reader: BufferedReader? = null

    fun connect(
        onConnected: () -> Unit,
        onMessageReceived: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                socket?.getOutputStream()?.let { stream ->
                    socket = Socket(serverIp, serverPort)
                    writer = PrintWriter(stream, true)
                    reader = BufferedReader(InputStreamReader(socket?.getInputStream()))
                    onConnected()
                    var line: String? = null
                    while (socket?.isConnected == true && reader?.readLine()
                            .also { line = it } != null
                    ) {
                        onMessageReceived(line ?: "")
                    }
                }
            } catch (e: Exception) {
                Log.e("TCPClient", "Error connecting to server: ${e.message}")
            } finally {
                disconnect()
            }
        }
    }

    fun listenToServer(onMessage: (String) -> Unit) {
        thread {
            try {
                val reader = BufferedReader(InputStreamReader(socket?.getInputStream()))
                var line: String? = null
                while (socket?.isConnected == true && reader?.readLine().also { line = it } != null) {
                    onMessage(line ?: "")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun sendMessage(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                writer?.println(message)
            } catch (e: Exception) {
                Log.e("TCPClient", "Error sending message: ${e.message}")
            }
        }
    }

    fun disconnect() {
        try {
            writer?.close()
            reader?.close()
            socket?.close()
        } catch (e: Exception) {
            Log.e("TCPClient", "Error disconnecting: ${e.message}")
        } finally {
            writer = null
            reader = null
            socket = null
        }
    }
}
