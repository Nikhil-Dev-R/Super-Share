package com.rudraksha.supershare.core.ui.screens.home

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rudraksha.supershare.core.utils.findActivity
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PermissionBottomSheet(
    grantedPermissionsFlow: StateFlow<Set<String>>,
    requiredPermissions: Set<String>,
    updatePermissionsStatus: (Set<String>) -> Unit,
    hasPermissions: (Set<String>) -> Boolean,
    onAllPermissionsGranted: () -> Unit,
    openAppSettings: (Context) -> Unit
) {
    val context = LocalContext.current
    val grantedPermissions by grantedPermissionsFlow.collectAsStateWithLifecycle()
    var grantButtonClicked by remember { mutableStateOf(false) }
    var showRationale by remember { mutableStateOf(false) }
    var granted by remember { mutableStateOf<Set<String>>(value = emptySet())}

    // Use a stable state map to avoid invalid recompositions
    val permissionLaunchers = remember {
        mutableMapOf<String, () -> Unit>()
    }

    requiredPermissions.forEach { permission ->
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val temp = granted.toMutableSet()
                temp.add(permission)
                granted = temp
                updatePermissionsStatus(granted)
            }
        }

        // Only set if not already set
        LaunchedEffect(Unit) {
            permissionLaunchers[permission] = {
                val activity = context.findActivity()
                activity?.let {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(it, permission)) {
                        showRationale = true
                    }
                }
                launcher.launch(permission)
            }
        }
    }

    LaunchedEffect(grantButtonClicked) {
        if (hasPermissions(requiredPermissions)) {
            onAllPermissionsGranted()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "The app needs the following permissions:",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        requiredPermissions.forEach { permission ->
            val isGranted = grantedPermissions.contains(permission)
            val label = permission.split(".").last()
            PermissionCard(
                label = label,
                enabled = !isGranted,
                onClick = {
                    permissionLaunchers[permission]?.invoke()
                    grantButtonClicked = !grantButtonClicked
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (showRationale) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Some permissions were denied. Please grant them from app settings for full functionality.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { openAppSettings(context) }) {
                Text("Open App Settings")
            }
        }
    }
}


@Composable
fun PermissionCard(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Card (
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
            )

            Button(
                onClick = onClick,
                enabled = enabled
            ) {
                Text(
                    text = if (!enabled) "Granted" else "Grant"
                )
            }
        }
    }
}