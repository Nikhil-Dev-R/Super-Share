package com.rudraksha.supershare.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rudraksha.supershare.core.domain.model.Screen
import com.rudraksha.supershare.core.ui.screens.discovery.DiscoveryScreen
import com.rudraksha.supershare.core.ui.screens.discovery.ReceiverWaitingScreen
import com.rudraksha.supershare.core.ui.screens.discovery.SenderWaitingScreen
import com.rudraksha.supershare.core.ui.screens.filemanager.PickerScreen
import com.rudraksha.supershare.core.ui.screens.history.HistoryScreen
import com.rudraksha.supershare.core.ui.screens.home.HomeScreen
import com.rudraksha.supershare.core.ui.screens.pcconnect.TransmissionModeScreen
import com.rudraksha.supershare.core.ui.screens.settings.SettingsScreen
import com.rudraksha.supershare.core.ui.screens.splash.SplashScreen
import com.rudraksha.supershare.core.ui.screens.transfer.TransferScreen
import com.rudraksha.supershare.core.viewmodel.DiscoveryUiState
import com.rudraksha.supershare.core.viewmodel.DiscoveryViewModel
import com.rudraksha.supershare.core.viewmodel.HistoryViewModel
import com.rudraksha.supershare.core.viewmodel.PermissionsViewModel
import com.rudraksha.supershare.core.viewmodel.PickerScreenViewModel
import com.rudraksha.supershare.core.viewmodel.SettingsViewModel
import com.rudraksha.supershare.core.viewmodel.TransferViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ShareAppNavHost(
    navController: NavHostController,
    hasPermissions: (Set<String>) -> Boolean,
) {
    val navigationManager = remember { NavigationManager(navController) }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                navigateToHome = {
                    navigationManager.navigate(Screen.Home, true)
                }
            )
        }

        composable(Screen.Home.route) {
            val permissionsViewModel: PermissionsViewModel = viewModel()
            HomeScreen(
                onSendClick = {
                    navigationManager.navigate(Screen.Picker)
                },
                onReceiveClick = {
                    navigationManager.navigate(Screen.ReceiverWaiting)
                },
                onHistoryClick = {
                    navigationManager.navigate(Screen.History)
                },
                onPCConnectClick = {
                    navigationManager.navigate(Screen.TransmissionMode)
                },
                onSettingsClick = {
                    navigationManager.navigate(Screen.Settings)
                },
                grantedPermissionsFlow = permissionsViewModel.grantedPermissions,
                requiredPermissions = permissionsViewModel.getRequiredPermissions(),
                hasPermissions = hasPermissions,
                updatePermissionsStatus = permissionsViewModel::updatePermissionsStatus,
                openAppSettings = permissionsViewModel::openAppSettings
            )
        }

        composable(Screen.Picker.route) {
            val viewModel: PickerScreenViewModel = viewModel()
            PickerScreen(
                observePickerUiState = viewModel.uiState,
                toggleItem = viewModel::toggleItemSelection,
                updateHistory = viewModel::toggleHistory,
                onNextClick = {
                    viewModel.onNextClicked()
                    navigationManager.navigate(Screen.SenderWaiting)
                }
            )
        }

        composable(Screen.SenderWaiting.route) {
            val viewModel: DiscoveryViewModel = viewModel()
            SenderWaitingScreen(
                uiStateFlow = viewModel.uiState,
                connectToDevice = viewModel::connectToDevice
            )
        }

        composable(Screen.ReceiverWaiting.route) {
            val viewModel: DiscoveryViewModel = viewModel()
            ReceiverWaitingScreen(
                uiStateFlow = viewModel.uiState,
                connectToDevice = viewModel::connectToDevice
            )
        }

        composable(Screen.Transfer.route) {
            val transferViewModel: TransferViewModel = viewModel()
            TransferScreen(
                transferViewModel.uiState
            )
        }

        composable(Screen.History.route) {
            val viewModel: HistoryViewModel = viewModel()
            HistoryScreen(
                observeUiState = viewModel.uiState,
                onBack = {
                    navigationManager.navigateUp()
                }
            )
        }

        composable(Screen.Settings.route) {
            val viewModel: SettingsViewModel = viewModel()
            SettingsScreen(
                observeUiState = viewModel.uiState,
                toggleDarkTheme = viewModel::toggleDarkTheme,
                toggleAutoAccept = viewModel::toggleAutoAccept,
                updateUsername = viewModel::updateUsername,
                saveCustomDeviceName = viewModel::saveCustomDeviceName,
                onBackClick = {
                    navigationManager.navigateUp()
                }
            )
        }

        composable(Screen.TransmissionMode.route) {
            TransmissionModeScreen({ mode ->
//                            navigationManager.navigateWithSerializable(Screen.ConnectReceiver, mode)
            }) {}
        }

//        composable(
//            route = Screen.ConnectReceiver.route,
//            arguments = listOf(navArgument("mode") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val modeArg = backStackEntry.arguments?.getString("mode")
//            val mode = Json.decodeFromString<Mode>(modeArg ?: "")
//            ConnectToReceiverScreen(mode)
//        }

        // If you have a screen expecting Serializable data:
//        composable(
//            route = "receiver_waiting/{data}",
//            arguments = listOf(navArgument("data") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val user = navigationManager.getSerializableArgument<YourSerializableClass>(
//                backStackEntry,
//                "data"
//            )
//            if (user != null) {
//                ReceiverWaitingScreen(navigationManager, user)
//            }
//        }
    }
}
