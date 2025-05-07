package com.rudraksha.supershare.core.data.manager

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log

class DeviceDiscoveryManager(private val context: Context) {

    fun promptUserToConnectToHotspot(ssid: String, password: String) {
        Log.d("DeviceDiscovery", "Prompting user to connect to SSID: $ssid")
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        context.startActivity(intent)
        // Also show a custom dialog/snackbar with credentials if needed
    }
}
