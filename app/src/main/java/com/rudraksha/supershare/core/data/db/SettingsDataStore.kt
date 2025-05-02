package com.rudraksha.supershare.core.data.db

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

private const val SETTINGS_PREFS = "settings_prefs"
private val Context.dataStore by preferencesDataStore(name = SETTINGS_PREFS)

