package com.rudraksha.supershare.core.ui.screens.filemanager

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LibraryMusic
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rudraksha.supershare.core.domain.model.AppItem
import com.rudraksha.supershare.core.domain.model.AudioItem
import com.rudraksha.supershare.core.domain.model.ContactItem
import com.rudraksha.supershare.core.domain.model.FileItem
import com.rudraksha.supershare.core.domain.model.MediaItem
import com.rudraksha.supershare.core.domain.model.ShareableItem
import com.rudraksha.supershare.core.domain.model.ShareableType
import com.rudraksha.supershare.core.ui.screens.history.HistoryItem
import kotlinx.coroutines.launch
import com.rudraksha.supershare.core.viewmodel.PickerUiState
import kotlinx.coroutines.flow.StateFlow

data class TabIconSet(
    val name: String,
    val filled: ImageVector,
    val outlined: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PickerScreen(
    observePickerUiState: StateFlow<PickerUiState>,
    toggleItem: (ShareableItem) -> Unit,
    updateHistory: (HistoryItem) -> Unit,
    onNextClick: () -> Unit,
) {
    val pickerUiState = observePickerUiState.collectAsState().value

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
                selectedCount = pickerUiState.selectedItems.size,
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
                        selectedItems = pickerUiState.history,
                        onToggle = updateHistory
                    )
                    1 -> AppsTab(
                        pickerUiState.apps,
                        pickerUiState.selectedItems.filterIsInstance<AppItem>(),
                        onToggle = toggleItem
                    )
                    2 -> FileTab(
                        pickerUiState.files,
                        pickerUiState.selectedItems.filterIsInstance<FileItem>(),
                        onToggle = toggleItem
                    )
                    3 -> VideoTab(
                        pickerUiState.videos,
                        pickerUiState.selectedItems.filterIsInstance<MediaItem>()
                            .filter { it.mediaType == ShareableType.VIDEO },
                        onToggle = toggleItem
                    )
                    4 -> PhotoTab(
                        pickerUiState.photos,
                        pickerUiState.selectedItems.filterIsInstance<MediaItem>()
                            .filter { it.mediaType == ShareableType.PHOTO },
                        onToggle = toggleItem
                    )
                    5 -> SongTab(
                        pickerUiState.songs,
                        pickerUiState.selectedItems.filterIsInstance<AudioItem>(),
                        onToggle = toggleItem
                    )
                    6 -> ContactsTab(
                        pickerUiState.contacts,
                        pickerUiState.selectedItems.filterIsInstance<ContactItem>(),
                        onToggle = toggleItem
                    )
                }
            }
        }
    }
}

/*
fun getInstalledApps(context: Context): List<AppItem> {
    val pm = context.packageManager
    return pm.getInstalledApplications(PackageManager.GET_META_DATA).mapNotNull { app ->
        try {
            val appName = pm.getApplicationLabel(app).toString()
            val packageInfo = pm.getPackageInfo(app.packageName, 0)
            val iconDrawable = pm.getApplicationIcon(app.packageName)

            AppItem(
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

fun getContacts(context: Context): List<ContactItem> {
    val contacts = mutableListOf<ContactItem>()
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
                ContactItem(
                    it.getString(nameIndex),
                    it.getString(phoneIndex)
                )
            )
        }
    }
    return contacts
}*/

val tabs = listOf(
    TabIconSet("History", Icons.Filled.History, Icons.Outlined.History),
    TabIconSet("Apps", Icons.Filled.Apps, Icons.Outlined.Apps),
    TabIconSet("Files", Icons.Filled.Folder, Icons.Outlined.Folder),
    TabIconSet("Videos", Icons.Filled.VideoLibrary, Icons.Outlined.VideoLibrary),
    TabIconSet("Photos", Icons.Filled.PhotoLibrary, Icons.Outlined.PhotoLibrary),
    TabIconSet("Songs", Icons.Filled.LibraryMusic, Icons.Outlined.LibraryMusic),
    TabIconSet("Contacts", Icons.Filled.Contacts, Icons.Outlined.Contacts)
)