package com.rudraksha.supershare.core.domain.repository

import android.content.Context
import android.os.Build
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.rudraksha.supershare.core.domain.model.Device
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.*

class NearbyDeviceDiscoveryRepository(
    private val context: Context
) : DeviceDiscoveryRepository {

    private val strategy = Strategy.P2P_CLUSTER
    private val serviceId = "com.rudraksha.supershare.SERVICE"
    private var isAdvertising = false
    private var isDiscovering = false
    private val endpointMap = mutableMapOf<String, Device>()

    private val connectionsClient by lazy { Nearby.getConnectionsClient(context) }

    private var connectionCallback: ((String, String) -> Unit)? = null

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            if (payload.type == Payload.Type.BYTES) {
                val message = payload.asBytes()?.decodeToString()
                if (message != null) {
                    connectionCallback?.invoke(endpointId, message)
                }
            }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            // You can update progress here if transferring files
        }
    }

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            connectionsClient.acceptConnection(endpointId, payloadCallback)
        }

        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            if (result.status.isSuccess) {
                // Connection established
                connectionCallback?.invoke(endpointId, "__connected__")
            } else {
                // Connection failed
                connectionCallback?.invoke(endpointId, "__failed__")
            }
        }

        override fun onDisconnected(endpointId: String) {
            connectionCallback?.invoke(endpointId, "__disconnected__")
        }
    }

    override fun discoverDevices(isSender: Boolean): Flow<List<Device>> = callbackFlow {
        val connectionsClient = Nearby.getConnectionsClient(context)

        val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
            override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
                val device = Device(
                    id = endpointId,
                    name = info.endpointName,
                    ipAddress = endpointId,
                )
                endpointMap[endpointId] = device
                trySend(endpointMap.values.toList())
            }

            override fun onEndpointLost(endpointId: String) {
                endpointMap.remove(endpointId)
                trySend(endpointMap.values.toList())
            }
        }

        if (isSender) {
            // Sender discovers receivers
            isDiscovering = true
            connectionsClient.startDiscovery(serviceId, endpointDiscoveryCallback, DiscoveryOptions.Builder().setStrategy(strategy).build())
        } else {
            // Receiver advertises itself
            isAdvertising = true
            val name = Build.MODEL + "-" + UUID.randomUUID().toString().take(4)
            connectionsClient.startAdvertising(
                name,
                serviceId,
                connectionLifecycleCallback,
                AdvertisingOptions.Builder().setStrategy(strategy).build()
            )
        }

        awaitClose {
            if (isSender && isDiscovering) {
                connectionsClient.stopDiscovery()
            } else if (!isSender && isAdvertising) {
                connectionsClient.stopAdvertising()
            }
            endpointMap.clear()
        }
    }

    override suspend fun connectToDevice(device: Device): Boolean {
        return try {
            Nearby.getConnectionsClient(context)
                .requestConnection(Build.MODEL, device.id, connectionLifecycleCallback)
                .await() // If this succeeds, connection request was sent
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun setOnMessageReceived(callback: (endpointId: String, message: String) -> Unit) {
        connectionCallback = callback
    }

    override fun sendMessageTo(endpointId: String, message: String) {
        val payload = Payload.fromBytes(message.toByteArray())
        connectionsClient.sendPayload(endpointId, payload)
    }

    override fun disconnectFrom(endpointId: String) {
        connectionsClient.disconnectFromEndpoint(endpointId)
    }
}