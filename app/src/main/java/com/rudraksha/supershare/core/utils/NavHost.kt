package com.rudraksha.supershare.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rudraksha.supershare.core.domain.model.Screen
import com.rudraksha.supershare.core.ui.screens.discovery.ReceiverWaitingScreen
import com.rudraksha.supershare.core.ui.screens.discovery.SenderWaitingScreen
import com.rudraksha.supershare.core.ui.screens.filemanager.AppInfo
import com.rudraksha.supershare.core.ui.screens.filemanager.ContactInfo
import com.rudraksha.supershare.core.ui.screens.filemanager.FileInfo
import com.rudraksha.supershare.core.ui.screens.filemanager.PhotoInfo
import com.rudraksha.supershare.core.ui.screens.filemanager.PickerScreen
import com.rudraksha.supershare.core.ui.screens.filemanager.SongInfo
import com.rudraksha.supershare.core.ui.screens.filemanager.VideoInfo
import com.rudraksha.supershare.core.ui.screens.history.HistoryItem
import com.rudraksha.supershare.core.ui.screens.history.HistoryScreen
import com.rudraksha.supershare.core.ui.screens.home.HomeScreen
import com.rudraksha.supershare.core.ui.screens.settings.SettingsScreen
import com.rudraksha.supershare.core.ui.screens.splash.SplashScreen
import com.rudraksha.supershare.core.ui.screens.transfer.TransferScreen

@Composable
fun ShareAppNavHost(navController: NavHostController) {
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
                    navigationManager.navigate(Screen.Settings)
                },
                onSettingsClick = {
                    navigationManager.navigate(Screen.Settings)
                }
            )
        }

        composable(Screen.Picker.route) {
            val dummyPainter = ColorPainter(Color.Gray)
            PickerScreen(
                apps = listOf(
                    AppInfo("WhatsApp", "32 MB", dummyPainter),
                    AppInfo("Instagram", "45 MB", dummyPainter),
                    AppInfo("WhatsApp2", "32 MB", dummyPainter),
                    AppInfo("Instagram2", "45 MB", dummyPainter),
                    AppInfo("WhatsApp", "32 MB", dummyPainter),
                    AppInfo("Instagram3", "45 MB", dummyPainter),
                ),
                files = listOf(
                    FileInfo("Document.pdf", "1.2 MB", dummyPainter),
                    FileInfo("Notes.txt", "250 KB", dummyPainter)
                ),
                videos = listOf(
                    VideoInfo("Vacation.mp4", "15 MB", "2:30", dummyPainter),
                    VideoInfo("Lecture.mov", "28 MB", "1:05:00", dummyPainter)
                ),
                photos = listOf(
                    PhotoInfo("IMG_001.jpg", "3.2 MB", "2024-10-11", dummyPainter),
                    PhotoInfo("Screenshot.png", "2.1 MB", "2024-10-12", dummyPainter)
                ),
                songs = listOf(
                    SongInfo("SongA.mp3", "5 MB", "3:45", dummyPainter),
                    SongInfo("TrackB.wav", "8 MB", "4:20", dummyPainter)
                ),
                contacts = listOf(
                    ContactInfo("Alice", "+123456789", dummyPainter),
                    ContactInfo("Bob", "+987654321", dummyPainter)
                ),
                selectedApps = listOf(AppInfo("WhatsApp", "32 MB", dummyPainter)),
                selectedFiles = emptyList(),
                selectedVideos = emptyList(),
                selectedPhotos = emptyList(),
                selectedSongs = listOf(SongInfo("SongA.mp3", "5 MB", "3:45", dummyPainter)),
                selectedContacts = listOf(ContactInfo("Alice", "+123456789", dummyPainter)),
                historyItems = listOf(
                    HistoryItem(
                        name = "A.mp3",
                        size = "5 MB",
                        type = "Song",
                        date = "2024-10-11",
                        icon = dummyPainter,
                    ),
                    HistoryItem(
                        name = "Wh",
                        size = "5 MB",
                        type = "App",
                        date = "2024-10-11",
                        icon = dummyPainter,
                    )
                ),
                onAppToggle = {},
                onFileToggle = {},
                onVideoToggle = {},
                onPhotoToggle = {},
                onSongToggle = {},
                onContactToggle = {},
                onNextClick = {
                    navigationManager.navigate(Screen.SenderWaiting)
                }
            )
        }

        composable(Screen.SenderWaiting.route) { SenderWaitingScreen(navigationManager) }

        composable(Screen.ReceiverWaiting.route) { ReceiverWaitingScreen(navigationManager) }

        composable(Screen.Transfer.route) { TransferScreen( viewModel()) }

        composable(Screen.History.route) {
            HistoryScreen(
                historyItems = listOf(),
                onBack = {
                    navigationManager.navigateUp()
                }
            )
        }

        composable(Screen.Settings.route) { SettingsScreen( viewModel() ) }

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
