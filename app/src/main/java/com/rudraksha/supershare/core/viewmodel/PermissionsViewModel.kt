package com.rudraksha.supershare.core.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudraksha.supershare.core.utils.permissionsApi33
import com.rudraksha.supershare.core.utils.permissionsLegacy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.core.net.toUri

class PermissionsViewModel : ViewModel() {

    private val _grantedPermissions = MutableStateFlow<Set<String>>(emptySet())
    val grantedPermissions: StateFlow<Set<String>> = _grantedPermissions.asStateFlow()

    fun updatePermissionsStatus(permissions: Set<String>) {
        _grantedPermissions.value = permissions
    }

    fun isPermissionGranted(permission: String): Boolean {
        return _grantedPermissions.value.contains(permission)
    }

    fun allPermissionsGranted(required: Set<String>): Boolean {
        return required.size == _grantedPermissions.value.size
    }

    fun getMissingPermissions(required: Set<String>): List<String> {
        return required.filterNot { _grantedPermissions.value.contains(it) }
    }

    fun reset() {
        _grantedPermissions.value = emptySet()
    }

    fun getRequiredPermissions(): Set<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsApi33
        } else {
            permissionsLegacy
        }
    }

    fun openAppSettings(context: Context) {
        viewModelScope.launch {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = "package:${context.packageName}".toUri()
            }
            context.startActivity(intent)
        }
    }
}
