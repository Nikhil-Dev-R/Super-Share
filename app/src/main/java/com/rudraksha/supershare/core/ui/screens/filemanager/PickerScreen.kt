package com.rudraksha.supershare.core.ui.screens.filemanager

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.provider.ContactsContract
import android.text.format.Formatter
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.SendAndArchive
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import androidx.core.graphics.createBitmap
import com.rudraksha.supershare.core.ui.screens.history.HistoryItem
import com.rudraksha.supershare.core.utils.toHistoryItem
import com.rudraksha.supershare.core.utils.toPainter

sealed class ShareableItem(
    open val name: String,
    open val size: String?,
    open val icon: Painter?,
    open val isSelected: Boolean = false
)

data class AppInfo(
    override val name: String,
    override val size: String,
    override val icon: Painter,
    override val isSelected: Boolean = false
) : ShareableItem(name, size, icon, isSelected)

data class FileInfo(
    override val name: String,
    override val size: String,
    override val icon: Painter? = null,
    override val isSelected: Boolean = false
) : ShareableItem(name, size, icon, isSelected)

data class VideoInfo(
    override val name: String,
    override val size: String,
    val duration: String,
    override val icon: Painter? = null,
    override val isSelected: Boolean = false
) : ShareableItem(name, size, icon, isSelected)

data class PhotoInfo(
    override val name: String,
    override val size: String,
    val date: String,
    override val icon: Painter? = null,
    override val isSelected: Boolean = false
) : ShareableItem(name, size, icon, isSelected)

data class SongInfo(
    override val name: String,
    override val size: String,
    val duration: String,
    override val icon: Painter? = null,
    override val isSelected: Boolean = false
) : ShareableItem(name, size, icon, isSelected)

data class ContactInfo(
    override val name: String,
    val phone: String,
    override val icon: Painter? = null,
    override val isSelected: Boolean = false
) : ShareableItem(name, null, icon, isSelected)

data class TabIconSet(
    val name: String,
    val filled: ImageVector,
    val outlined: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PickerScreen(
    apps: List<AppInfo>,
    files: List<FileInfo>,
    videos: List<VideoInfo>,
    photos: List<PhotoInfo>,
    songs: List<SongInfo>,
    contacts: List<ContactInfo>,
    selectedApps: List<AppInfo>,
    selectedFiles: List<FileInfo>,
    selectedVideos: List<VideoInfo>,
    selectedPhotos: List<PhotoInfo>,
    selectedSongs: List<SongInfo>,
    selectedContacts: List<ContactInfo>,
    historyItems: List<HistoryItem>,
    onAppToggle: (AppInfo) -> Unit,
    onFileToggle: (FileInfo) -> Unit,
    onVideoToggle: (VideoInfo) -> Unit,
    onPhotoToggle: (PhotoInfo) -> Unit,
    onSongToggle: (SongInfo) -> Unit,
    onContactToggle: (ContactInfo) -> Unit,
    onNextClick: () -> Unit
) {
    val tabs = listOf(
        TabIconSet("History", Icons.Filled.History, Icons.Outlined.History),
        TabIconSet("Apps", Icons.Filled.Apps, Icons.Outlined.Apps),
        TabIconSet("Files", Icons.Filled.Folder, Icons.Outlined.Folder),
        TabIconSet("Videos", Icons.Filled.VideoLibrary, Icons.Outlined.VideoLibrary),
        TabIconSet("Photos", Icons.Filled.PhotoLibrary, Icons.Outlined.PhotoLibrary),
        TabIconSet("Songs", Icons.Filled.LibraryMusic, Icons.Outlined.LibraryMusic),
        TabIconSet("Contacts", Icons.Filled.Contacts, Icons.Outlined.Contacts)
    )
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { tabs.size }
    )
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Send", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(
                selectedCount = selectedApps.size + selectedFiles.size + selectedVideos.size +
                        selectedPhotos.size + selectedSongs.size + selectedContacts.size,
                onNextClick = onNextClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp), // Removes default side padding
                edgePadding = 0.dp, // This specifically removes the left/right internal gap
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        height = 3.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                tabs.forEachIndexed { index, tab ->
                    val selected = pagerState.currentPage == index
                    Tab(
                        selected = selected,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(tab.name) },
                        icon = {
                            Crossfade(targetState = selected, label = "${tab.name}_IconFade") {
                                Icon(
                                    imageVector = if (it) tab.filled else tab.outlined,
                                    contentDescription = tab.name
                                )
                            }
                        },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> HistoryTab(
                        selectedItems = historyItems,
                        onToggle = { /* Provide toggle logic if needed */ }
                    )
                    1 -> AppsTab(apps, selectedApps, onAppToggle)
                    2 -> FileTab(files, selectedFiles, onFileToggle)
                    3 -> VideoTab(videos, selectedVideos, onVideoToggle)
                    4 -> PhotoTab(photos, selectedPhotos, onPhotoToggle)
                    5 -> SongTab(songs, selectedSongs, onSongToggle)
                    6 -> ContactsTab(contacts, selectedContacts, onContactToggle)
                }
            }
        }
    }
}

fun getInstalledApps(context: Context): List<AppInfo> {
    val pm = context.packageManager
    return pm.getInstalledApplications(PackageManager.GET_META_DATA).mapNotNull { app ->
        try {
            val appName = pm.getApplicationLabel(app).toString()
            val packageInfo = pm.getPackageInfo(app.packageName, 0)
            val iconDrawable = pm.getApplicationIcon(app.packageName)

            AppInfo(
                name = appName,
                size = packageInfo.applicationInfo?.publicSourceDir?.let {
                    Formatter.formatShortFileSize(context, File(it).length())
                } ?: "0",
                icon = iconDrawable.toPainter()
            )
        } catch (e: Exception) {
            null
        }
    }
}

fun getContacts(context: Context): List<ContactInfo> {
    val contacts = mutableListOf<ContactInfo>()
    val cursor = context.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null, null, null, null
    )
    cursor?.use {
        while (it.moveToNext()) {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val phoneIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            if (nameIndex < 0 || phoneIndex < 0) continue

            contacts.add(
                ContactInfo(
                    it.getString(nameIndex),
                    it.getString(phoneIndex)
                )
            )
        }
    }
    return contacts
}
