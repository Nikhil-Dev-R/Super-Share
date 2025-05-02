package com.rudraksha.supershare.core.viewmodel

import android.app.Application
import android.os.Build
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rudraksha.supershare.core.data.datastore.DeviceNameDataStore
import com.rudraksha.supershare.core.data.datastore.SettingsDataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SettingsUiState(
    val darkTheme: Boolean = false,
    val autoAccept: Boolean = false,
    val username: String = "User",
    val deviceName: String = ""
)

class SettingsViewModel(
    private val dataStore: SettingsDataStore,
    private val app: Application
) : AndroidViewModel(app) {

    private val _deviceName = MutableStateFlow("")
    val deviceName = _deviceName.asStateFlow()

    val uiState: StateFlow<SettingsUiState> = combine(
        dataStore.isDarkTheme,
        dataStore.autoAccept,
        dataStore.username,
        _deviceName
    ) { darkTheme, autoAccept, username, deviceName ->
        SettingsUiState(darkTheme, autoAccept, username, deviceName)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsUiState())

    init {
        loadDeviceName()
    }

    private fun loadDeviceName() {
        viewModelScope.launch {
            val custom = DeviceNameDataStore.getCustomDeviceName(app)
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
        dataStore.setDarkTheme(!uiState.value.darkTheme)
    }

    fun toggleAutoAccept() = viewModelScope.launch {
        dataStore.setAutoAccept(!uiState.value.autoAccept)
    }

    fun updateUsername(name: String) = viewModelScope.launch {
        dataStore.setUsername(name)
    }

    fun saveCustomDeviceName(name: String) {
        viewModelScope.launch {
            DeviceNameDataStore.saveCustomDeviceName(app, name)
            _deviceName.value = name
        }
    }
}
