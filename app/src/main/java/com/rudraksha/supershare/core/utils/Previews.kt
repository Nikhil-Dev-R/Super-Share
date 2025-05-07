package com.rudraksha.supershare.core.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rudraksha.supershare.core.domain.model.NetworkMode
import com.rudraksha.supershare.core.domain.model.ShareableType
import com.rudraksha.supershare.core.ui.components.SuperShareSearchBar
import com.rudraksha.supershare.core.ui.components.SuperShareTopBar
import com.rudraksha.supershare.core.ui.screens.discovery.DiscoveryScreen
import com.rudraksha.supershare.core.ui.screens.filemanager.PickerScreen
import com.rudraksha.supershare.core.ui.screens.history.HistoryItem
import com.rudraksha.supershare.core.ui.screens.history.HistoryScreen
import com.rudraksha.supershare.core.ui.screens.home.HomeScreen
import com.rudraksha.supershare.core.ui.screens.home.PermissionBottomSheet
import com.rudraksha.supershare.core.ui.screens.pcconnect.ConnectToReceiverScreen
import com.rudraksha.supershare.core.ui.screens.settings.SettingsScreen
import com.rudraksha.supershare.core.viewmodel.DiscoveryUiState
import com.rudraksha.supershare.core.viewmodel.HistoryUiState
import com.rudraksha.supershare.core.viewmodel.PermissionsViewModel
import com.rudraksha.supershare.core.viewmodel.PickerScreenViewModel
import com.rudraksha.supershare.core.viewmodel.SettingsUiState
import com.rudraksha.supershare.ui.theme.SuperShareTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

//@Preview
@Composable
fun TopBarPreview() {
    SuperShareTheme {
        SuperShareTopBar(
            title = "Title",
//            showBackButton = true,
            searchBar = {
                SuperShareSearchBar(
                    query = "",
                    onQueryChange = {}
                )
            },
            centerTitle = false,
        )
    }
}

//@Preview
@Composable
fun HomeSPreview() {
    val viewModel: PermissionsViewModel = viewModel()
    SuperShareTheme {
        HomeScreen(
            onSendClick = {},
            onReceiveClick = {},
            {}, {}, {},
            grantedPermissionsFlow = MutableStateFlow(
                setOf(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE"
                )
            ).asStateFlow(),
            hasPermissions = { false },
            requiredPermissions = viewModel.getRequiredPermissions(),
            updatePermissionsStatus = {},
        ) {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PermissionsSPreview() {
    PermissionBottomSheet(
        grantedPermissionsFlow = MutableStateFlow(
            setOf(
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
            )
        ).asStateFlow(),
        requiredPermissions = permissionsLegacy,
        updatePermissionsStatus = {},
        onAllPermissionsGranted = {},
        hasPermissions = { false }
    ) {}
}

//@Preview
@Composable
fun HSPreview() {
    SuperShareTheme {
        HistoryScreen (
            observeUiState = MutableStateFlow(
                HistoryUiState(
                    listOf(
                        HistoryItem(
                            name = "File 1",
                            size = 1024.toString(),
                            type = ShareableType.FILE,
                            icon = null,
                        ),
                        HistoryItem(
                            name = "File 2",
                            size = 1024.toString(),
                            type = ShareableType.FILE,
                            icon = null,
                        ),
                        HistoryItem(
                            name = "File 0",
                            size = 1024.toString(),
                            type = ShareableType.APP,
                            icon = null,
                        ),
                        HistoryItem(
                            name = "File 1",
                            size = 1024.toString(),
                            type = ShareableType.AUDIO,
                            icon = null,
                        )
                    )
                )
            ).asStateFlow()
        ) {}
    }
}

//@Preview
@Composable
fun PCPreview() {
    SuperShareTheme {
        ConnectToReceiverScreen(
            networkMode = NetworkMode.Hotspot
        )
    }
}

//@Preview
@Composable
fun DSPreview() {
    val uiStateFlow: StateFlow<DiscoveryUiState> = MutableStateFlow(DiscoveryUiState())
    DiscoveryScreen(
        uiStateFlow,
        toggleSenderReceiver = {},
        connectToDevice = {}
    )
}

//@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen(
        observeUiState = MutableStateFlow<SettingsUiState>(SettingsUiState(
            darkTheme = true,
            autoAccept = false,
            username = "Nikhil",
            deviceName = "Pixel 7 Pro"
        ) ).asStateFlow(),
        toggleDarkTheme = {},
        toggleAutoAccept = {},
        updateUsername = {},
        saveCustomDeviceName = {},
        onBackClick = {}
    )
}

//@Preview(showBackground = true)
@Composable
fun PSPreview() {
    val viewModel: PickerScreenViewModel = viewModel()
    SuperShareTheme {
        PickerScreen(
            observePickerUiState = viewModel.uiState,
            toggleItem = {},
            onNextClick = viewModel::onNextClicked,
            updateHistory = viewModel::toggleHistory
        )
    }
}
