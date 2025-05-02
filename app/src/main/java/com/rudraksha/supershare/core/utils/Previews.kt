package com.rudraksha.supershare.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rudraksha.supershare.core.ui.screens.discovery.DiscoveryScreen
import com.rudraksha.supershare.core.ui.screens.filemanager.AppInfo
import com.rudraksha.supershare.core.ui.screens.filemanager.ContactInfo
import com.rudraksha.supershare.core.ui.screens.filemanager.FileInfo
import com.rudraksha.supershare.core.ui.screens.filemanager.PhotoInfo
import com.rudraksha.supershare.core.ui.screens.filemanager.PickerScreen
import com.rudraksha.supershare.core.ui.screens.filemanager.SongInfo
import com.rudraksha.supershare.core.ui.screens.filemanager.VideoInfo
import com.rudraksha.supershare.core.ui.screens.history.HistoryItem
import com.rudraksha.supershare.core.ui.screens.home.HomeScreen
import com.rudraksha.supershare.core.ui.screens.settings.SettingsContent
import com.rudraksha.supershare.core.ui.screens.settings.SettingsScreen
import com.rudraksha.supershare.core.viewmodel.SettingsUiState
import com.rudraksha.supershare.ui.theme.SuperShareTheme

//@Preview
@Composable
fun HomeSPreview() {
    SuperShareTheme {
        HomeScreen(
            onSendClick = {},
            onReceiveClick = {}
        )
    }
}

//@Preview
@Composable
fun DSPreview() {
    DiscoveryScreen(viewModel = viewModel())
}

//@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    SettingsContent(
        uiState = SettingsUiState(
            darkTheme = true,
            autoAccept = false,
            username = "Nikhil",
            deviceName = "Pixel 7 Pro"
        ),
        onToggleDarkTheme = {},
        onToggleAutoAccept = {},
        onUsernameChange = {},
        onDeviceNameChange = {}
    )
}

//@Preview(showBackground = true)
@Composable
fun PSPreview() {
    val dummyPainter = ColorPainter(Color.Gray)

    SuperShareTheme {
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
            onNextClick = {}
        )
    }
}
