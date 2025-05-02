package com.rudraksha.supershare.core.viewmodel

import androidx.lifecycle.ViewModel
import com.rudraksha.supershare.core.domain.model.Device
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class DiscoveryUiState(
    val isSender: Boolean = true,
    val isSearching: Boolean = true,
    val devices: List<Device> = emptyList()
)

class DiscoveryViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DiscoveryUiState())
    val uiState: StateFlow<DiscoveryUiState> = _uiState

    fun toggleSenderReceiver() {
        _uiState.update { it.copy(isSender = !it.isSender, isSearching = true) }
        // Trigger discovery logic...
    }

    fun connectToDevice(device: Device) {
        // Handle connection logic here
    }
}
