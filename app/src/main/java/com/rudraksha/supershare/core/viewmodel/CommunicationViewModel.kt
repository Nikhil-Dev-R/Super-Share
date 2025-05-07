package com.rudraksha.supershare.core.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rudraksha.supershare.core.data.manager.HotspotManager
import com.rudraksha.supershare.core.data.manager.TCPServer
import com.rudraksha.supershare.core.data.manager.TCPClient

class CommunicationViewModel(application: Application) : AndroidViewModel(application) {

    private val hotspotManager = HotspotManager(application.applicationContext)
    private val tcpServer = TCPServer()
    private var tcpClient: TCPClient? = null

    private val _serverStatus = MutableLiveData<String>()
    val serverStatus: LiveData<String> = _serverStatus

    private val _clientStatus = MutableLiveData<String>()
    val clientStatus: LiveData<String> = _clientStatus

    private val _messageReceived = MutableLiveData<String>()
    val messageReceived: LiveData<String> = _messageReceived

    // Start the hotspot server and TCP listener
    fun startServer() {
        _serverStatus.postValue("Starting hotspot...")

        hotspotManager.startHotspot(
            onStarted = { ssid, password ->
                _serverStatus.postValue("Hotspot Started\nSSID: $ssid\nPassword: $password")

                tcpServer.startServer { clientSocket ->
                    Log.d("CommunicationVM", "Client connected: ${clientSocket.inetAddress.hostAddress}")
                    _serverStatus.postValue("Client connected: ${clientSocket.inetAddress.hostAddress}")

                    // Handle incoming messages (example: read message)
                    tcpServer.listenToClient(clientSocket) { message ->
                        _messageReceived.postValue("Client: $message")
                    }
                }
            },
            onFailed = { code, reason ->
                _serverStatus.postValue("Hotspot failed: Code $code - $reason")
            }
        )
    }

    // Stop the TCP server and hotspot
    fun stopServer() {
        tcpServer.stopServer()
        hotspotManager.stopHotspot()
        _serverStatus.postValue("Server & Hotspot stopped.")
    }

    // Connect as a TCP client to server IP
    fun connectToServer(serverIp: String) {
        _clientStatus.postValue("Connecting to server...")

        tcpClient = TCPClient(serverIp)
        tcpClient?.connect(
            onConnected = {
                _clientStatus.postValue("Connected to server.")
                tcpClient?.listenToServer { message ->
                    _messageReceived.postValue("Server: $message")
                }
            },
            onMessageReceived = { message ->
                _messageReceived.postValue("Received from server: $message")
            }
        )
    }

    // Send message from client to server
    fun sendMessage(message: String) {
        if (tcpClient == null) {
            _clientStatus.postValue("Client not connected")
            return
        }
        tcpClient?.sendMessage(message)
    }

    // Disconnect TCP client
    fun disconnectClient() {
        tcpClient?.disconnect()
        _clientStatus.postValue("Client disconnected")
    }
}
