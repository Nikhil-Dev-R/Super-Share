package com.rudraksha.supershare.core.ui.screens.discovery

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rudraksha.supershare.core.domain.model.Device
import com.rudraksha.supershare.core.utils.NavigationManager
import com.rudraksha.supershare.core.viewmodel.DiscoveryUiState
import com.rudraksha.supershare.core.viewmodel.DiscoveryViewModel
import com.rudraksha.supershare.core.viewmodel.HotspotViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun DiscoveryScreen(
    uiStateFlow: StateFlow<DiscoveryUiState>,
    toggleSenderReceiver: () -> Unit,
    connectToDevice: (Device) -> Unit
) {
    val uiState by uiStateFlow.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Discovery", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        ModeToggle(
            isSender = uiState.isSender,
            onToggle = { toggleSenderReceiver() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.isSearching) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Text("Searching for nearby devices...", style = MaterialTheme.typography.bodyMedium)
        } else {
            Text("Nearby Devices", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            DeviceList(devices = uiState.devices, onDeviceClick = connectToDevice)
        }
    }
}

@Composable
fun ModeToggle(
    isSender: Boolean,
    onToggle: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 3.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = isSender,
                onClick = { if (!isSender) onToggle() },
                label = { Text("Send") },
                leadingIcon = { Icon(Icons.AutoMirrored.Default.Send, contentDescription = null) }
            )
            FilterChip(
                selected = !isSender,
                onClick = { if (isSender) onToggle() },
                label = { Text("Receive") },
                leadingIcon = { Icon(Icons.Default.WifiTethering, contentDescription = null) }
            )
        }
    }
}

@Composable
fun DeviceList(
    devices: List<Device>,
    onDeviceClick: (Device) -> Unit
) {
    if (devices.isEmpty()) {
        Text("No devices found", color = Color.Gray, modifier = Modifier.padding(8.dp))
    } else {
        LazyColumn {
            items(devices.size) { index ->
                val device = devices[index]
                DeviceItem(device = device, onClick = { onDeviceClick(device) })
            }
        }
    }
}

@Composable
fun DeviceItem(device: Device, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = Icons.Filled.Devices,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = device.name, fontWeight = FontWeight.Medium)
            Text(text = device.ipAddress, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun SenderWaitingScreen(
    uiStateFlow: StateFlow<DiscoveryUiState>,
    connectToDevice: (Device) -> Unit
) {
    val uiState by uiStateFlow.collectAsStateWithLifecycle()
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.WifiTethering,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Waiting for receiver to connect…", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(32.dp))

            CircularProgressIndicator()
            Text("Nearby Devices", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            DeviceList(devices = uiState.devices, onDeviceClick = connectToDevice)
        }
    }
}

@Composable
fun ReceiverWaitingScreen(
    uiStateFlow: StateFlow<DiscoveryUiState>,
    connectToDevice: (Device) -> Unit
) {
    val uiState by uiStateFlow.collectAsStateWithLifecycle()
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.WifiTethering,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Searching for sender…", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(32.dp))

            CircularProgressIndicator()
        }
    }
}

@Composable
fun HotspotScreen(viewModel: HotspotViewModel) {
    val state by viewModel.hotspotState.collectAsState()

    Column {
        if (state.isRunning) {
            Text("Hotspot is running: SSID=${state.ssid}, Pass=${state.password}")
            Button(onClick = viewModel::stopHotspot) { Text("Stop Hotspot") }
        } else {
            Button(onClick = viewModel::startHotspot) { Text("Start Hotspot") }
            state.error?.let { Text("Error: $it", color = Color.Red) }
        }

        if (state.isRunning) {
            Button(onClick = viewModel::promptUserToConnect) { Text("Show Wi-Fi Settings") }
        }
    }
}
