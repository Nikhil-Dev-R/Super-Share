package com.rudraksha.supershare.core.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rudraksha.supershare.core.data.manager.HotspotManager
import com.rudraksha.supershare.core.data.manager.DeviceDiscoveryManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HotspotState(
    val isRunning: Boolean = false,
    val ssid: String? = null,
    val password: String? = null,
    val error: String? = null
)

class HotspotViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val hotspotManager = HotspotManager(context)
    private val discoveryManager = DeviceDiscoveryManager(context)

    private val _hotspotState = MutableStateFlow(HotspotState())
    val hotspotState: StateFlow<HotspotState> = _hotspotState

    fun startHotspot() {
        viewModelScope.launch {
            hotspotManager.startHotspot(
                onStarted = { ssid, password ->
                    _hotspotState.value = HotspotState(true, ssid, password)
                },
                onFailed = { _, message ->
                    _hotspotState.value = HotspotState(false, null, null, message)
                }
            )
        }
    }

    fun stopHotspot() {
        hotspotManager.stopHotspot()
        _hotspotState.value = HotspotState()
    }

    fun promptUserToConnect() {
        _hotspotState.value.ssid?.let { ssid ->
            _hotspotState.value.password?.let { password ->
                discoveryManager.promptUserToConnectToHotspot(ssid, password)
            }
        }
    }
}
