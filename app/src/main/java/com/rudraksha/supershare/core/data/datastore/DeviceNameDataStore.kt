package com.rudraksha.supershare.core.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rudraksha.supershare.core.utils.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DeviceNameDataStore(private val context: Context) {
    private val DEVICE_NAME_KEY = stringPreferencesKey("custom_device_name")

    suspend fun saveCustomDeviceName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[DEVICE_NAME_KEY] = name
        }
    }

    suspend fun getCustomDeviceName(): String? {
        return context.dataStore.data
            .map { it[DEVICE_NAME_KEY] }
            .first()
    }
}

