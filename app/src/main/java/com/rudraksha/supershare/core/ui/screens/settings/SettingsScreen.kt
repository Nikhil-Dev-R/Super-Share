package com.rudraksha.supershare.core.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rudraksha.supershare.core.viewmodel.SettingsUiState
import com.rudraksha.supershare.core.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    SettingsContent(
        uiState = uiState,
        onToggleDarkTheme = { viewModel.toggleDarkTheme() },
        onToggleAutoAccept = { viewModel.toggleAutoAccept() },
        onUsernameChange = { viewModel.updateUsername(it) },
        onDeviceNameChange = { viewModel.saveCustomDeviceName(it) }
    )
}

@Composable
fun SettingsContent(
    uiState: SettingsUiState,
    onToggleDarkTheme: () -> Unit,
    onToggleAutoAccept: () -> Unit,
    onUsernameChange: (String) -> Unit,
    onDeviceNameChange: (String) -> Unit
) {
    var editingUsername by remember { mutableStateOf(false) }
    var editingDeviceName by remember { mutableStateOf(false) }
    var usernameInput by remember { mutableStateOf(TextFieldValue(uiState.username)) }
    var deviceNameInput by remember { mutableStateOf(TextFieldValue(uiState.deviceName)) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(12.dp)) {

        Text("Settings", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

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
                onUsernameChange(usernameInput.text)
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
                onDeviceNameChange(deviceNameInput.text)
                editingDeviceName = false
            }
        )
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
                .padding(4.dp),
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
    input: TextFieldValue,
    onInputChange: (TextFieldValue) -> Unit,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(16.dp))
            Text(title, style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(Modifier.height(4.dp))

        Card {
            if (isEditing) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = input,
                        onValueChange = onInputChange,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                    IconButton(
                        onClick = onSaveClick
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = "Save"
                        )
                    }
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 40.dp)
                ) {
                    Text(
                        value,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onEditClick
                    ) {
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