package com.rudraksha.supershare.core.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.rudraksha.supershare.core.ui.components.SuperShareTopBar
import com.rudraksha.supershare.core.viewmodel.SettingsUiState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SettingsScreen(
    observeUiState: StateFlow<SettingsUiState>,
    toggleDarkTheme: () -> Unit,
    toggleAutoAccept: () -> Unit,
    updateUsername: (String) -> Unit,
    saveCustomDeviceName: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by observeUiState.collectAsState()

    SettingsContent(
        uiState = uiState,
        onToggleDarkTheme = { toggleDarkTheme() },
        onToggleAutoAccept = { toggleAutoAccept() },
        onUsernameChange = { updateUsername(it) },
        onDeviceNameChange = { saveCustomDeviceName(it) },
        onBackClick = onBackClick
    )
}

@Composable
fun SettingsContent(
    uiState: SettingsUiState,
    onToggleDarkTheme: () -> Unit,
    onToggleAutoAccept: () -> Unit,
    onUsernameChange: (String) -> Unit,
    onDeviceNameChange: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var editingUsername by remember { mutableStateOf(false) }
    var editingDeviceName by remember { mutableStateOf(false) }
    var usernameInput by remember { mutableStateOf(uiState.username) }
    var deviceNameInput by remember { mutableStateOf(uiState.deviceName) }

    Scaffold(
        topBar = {
            SuperShareTopBar(
                title = "Settings",
                showBackButton = true,
                onBackClick = onBackClick,
                centerTitle = false
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(12.dp)
        ) {
            SettingToggleItem(
                title = "Dark Theme",
                checked = uiState.darkTheme,
                onCheckedChange = { onToggleDarkTheme() },
                icon = Icons.Default.DarkMode
            )

            Spacer(Modifier.height(16.dp))

            SettingToggleItem(
                title = "Auto-Accept Files",
                checked = uiState.autoAccept,
                onCheckedChange = { onToggleAutoAccept() },
                icon = Icons.Default.Settings
            )

            Spacer(Modifier.height(16.dp))

            EditableTextItem(
                title = "Username",
                value = uiState.username,
                icon = Icons.Default.Person,
                isEditing = editingUsername,
                input = usernameInput,
                onInputChange = { usernameInput = it },
                onEditClick = { editingUsername = true },
                onSaveClick = {
                    onUsernameChange(usernameInput)
                    editingUsername = false
                }
            )

            Spacer(Modifier.height(16.dp))

            EditableTextItem(
                title = "Device Name",
                value = uiState.deviceName,
                icon = Icons.Default.PhoneAndroid,
                isEditing = editingDeviceName,
                input = deviceNameInput,
                onInputChange = { deviceNameInput = it },
                onEditClick = { editingDeviceName = true },
                onSaveClick = {
                    onDeviceNameChange(deviceNameInput)
                    editingDeviceName = false
                }
            )
        }
    }
}

@Composable
fun SettingToggleItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: ImageVector
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}

@Composable
fun EditableTextItem(
    title: String,
    value: String,
    icon: ImageVector,
    isEditing: Boolean,
    input: String,
    onInputChange: (String) -> Unit,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(Modifier.width(16.dp))
            Text(title, style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(Modifier.height(4.dp))

        Card {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))

                Spacer(modifier = Modifier.width(16.dp))

                if (isEditing) {
                    BasicTextField(
                        value = input,
                        onValueChange = onInputChange,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        singleLine = true,
                    )

                    IconButton(onClick = onSaveClick) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Save"
                        )
                    }
                } else {
                    Text(
                        value,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit"
                        )
                    }
                }
            }
        }
    }
}