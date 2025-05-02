package com.rudraksha.supershare.core.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rudraksha.supershare.core.utils.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object DeviceNameDataStore {
    private val DEVICE_NAME_KEY = stringPreferencesKey("custom_device_name")

    suspend fun saveCustomDeviceName(context: Context, name: String) {
        context.dataStore.edit { prefs ->
            prefs[DEVICE_NAME_KEY] = name
        }
    }

    suspend fun getCustomDeviceName(context: Context): String? {
        return context.dataStore.data
            .map { it[DEVICE_NAME_KEY] }
            .first()
    }
}