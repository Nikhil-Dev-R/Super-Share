package com.rudraksha.supershare.core.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rudraksha.supershare.core.utils.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsDataStore(private val context: Context) {

    companion object {
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val AUTO_ACCEPT = booleanPreferencesKey("auto_accept")
        val USERNAME = stringPreferencesKey("username")
    }

    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { it[DARK_THEME] == true }

    val autoAccept: Flow<Boolean> = context.dataStore.data
        .map { it[AUTO_ACCEPT] == true }

    val username: Flow<String> = context.dataStore.data
        .map { it[USERNAME] ?: "User" }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.dataStore.edit { it[DARK_THEME] = enabled }
    }

    suspend fun setAutoAccept(enabled: Boolean) {
        context.dataStore.edit { it[AUTO_ACCEPT] = enabled }
    }

    suspend fun setUsername(name: String) {
        context.dataStore.edit { it[USERNAME] = name }
    }
}