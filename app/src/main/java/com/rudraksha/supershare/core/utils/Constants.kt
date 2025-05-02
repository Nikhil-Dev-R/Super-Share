package com.rudraksha.supershare.core.utils

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

const val SETTINGS_PREFS = "settings_prefs"
val Context.dataStore by preferencesDataStore(name = SETTINGS_PREFS)