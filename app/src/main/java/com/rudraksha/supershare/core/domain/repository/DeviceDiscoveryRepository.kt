package com.rudraksha.supershare.core.domain.repository

import com.rudraksha.supershare.core.domain.model.Device
import kotlinx.coroutines.flow.Flow

interface DeviceDiscoveryRepository {
    fun discoverDevices(isSender: Boolean): Flow<List<Device>>
    suspend fun connectToDevice(device: Device): Boolean
    fun setOnMessageReceived(callback: (endpointId: String, message: String) -> Unit)
    fun sendMessageTo(endpointId: String, message: String)
    fun disconnectFrom(endpointId: String)
}
