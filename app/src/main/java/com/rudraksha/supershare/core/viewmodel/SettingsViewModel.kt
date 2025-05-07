package com.rudraksha.supershare.core.viewmodel

import android.os.Build
import android.app.Application
import android.provider.Settings
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.MutableStateFlow
import com.rudraksha.supershare.core.data.datastore.SettingsDataStore
import com.rudraksha.supershare.core.data.datastore.DeviceNameDataStore

data class SettingsUiState(
    val darkTheme: Boolean = false,
    val autoAccept: Boolean = false,
    val username: String = "User",
    val deviceName: String = ""
)

class SettingsViewModel(
    private val app: Application,
) : AndroidViewModel(app) {

    private val settingsDataStore: SettingsDataStore = SettingsDataStore(app)
    private val deviceNameDataStore: DeviceNameDataStore = DeviceNameDataStore(app)
    private val _deviceName = MutableStateFlow("")
    val deviceName = _deviceName.asStateFlow()

    val uiState: StateFlow<SettingsUiState> = combine(
        settingsDataStore.isDarkTheme,
        settingsDataStore.autoAccept,
        settingsDataStore.username,
        _deviceName
    ) { darkTheme, autoAccept, username, deviceName ->
        SettingsUiState(darkTheme, autoAccept, username, deviceName)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsUiState())

    init {
        loadDeviceName()
    }

    private fun loadDeviceName() {
        viewModelScope.launch {
            val custom = deviceNameDataStore.getCustomDeviceName()
            _deviceName.value = custom ?: getSystemDeviceName()
        }
    }

    private fun getSystemDeviceName(): String {
        return try {
            Settings.Global.getString(app.contentResolver, "device_name")
                ?: defaultDeviceName()
        } catch (e: Exception) {
            defaultDeviceName()
        }
    }

    private fun defaultDeviceName(): String {
        val manufacturer = Build.MANUFACTURER.replaceFirstChar { it.uppercase() }
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) model else "$manufacturer $model"
    }

    fun toggleDarkTheme() = viewModelScope.launch {
        settingsDataStore.setDarkTheme(!uiState.value.darkTheme)
    }

    fun toggleAutoAccept() = viewModelScope.launch {
        settingsDataStore.setAutoAccept(!uiState.value.autoAccept)
    }

    fun updateUsername(name: String) = viewModelScope.launch {
        settingsDataStore.setUsername(name)
    }

    fun saveCustomDeviceName(name: String) {
        viewModelScope.launch {
            deviceNameDataStore.saveCustomDeviceName(name)
            _deviceName.value = name
        }
    }
}
