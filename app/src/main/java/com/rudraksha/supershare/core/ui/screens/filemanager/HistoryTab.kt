package com.rudraksha.supershare.core.ui.screens.filemanager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rudraksha.supershare.core.ui.components.FilterRow
import com.rudraksha.supershare.core.ui.screens.history.HistoryFilter
import com.rudraksha.supershare.core.ui.screens.history.HistoryItem
import com.rudraksha.supershare.core.ui.screens.history.HistoryListItem

@Composable
fun HistoryTab(
    selectedItems: List<HistoryItem>,
    onToggle: (HistoryItem) -> Unit
) {
    var selectedFilter by remember { mutableStateOf(HistoryFilter.TYPE) }

    Column {
        FilterRow(selectedFilter = selectedFilter, onFilterChange = {
            selectedFilter = it
        })

        val grouped = remember(selectedItems, selectedFilter) {
            when (selectedFilter) {
                HistoryFilter.TYPE -> selectedItems.groupBy<HistoryItem, String> { it.type.toString() }
                HistoryFilter.SIZE -> selectedItems.groupBy<HistoryItem, String> { it.size }
                HistoryFilter.DATE -> selectedItems.groupBy<HistoryItem, String> { it.date.toString() }
            }
        }

        LazyColumn {
            grouped.forEach { (groupKey, groupItems) ->
                item {
                    Text(
                        text = groupKey,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                items(groupItems) { item ->
                    HistoryListItem(item, selectedItems.contains(item)) {
                        onToggle(item)
                    }
                }
            }
        }
    }
}


/*@Composable
fun HistoryTab(
    selectedApps: List<AppInfo>,
    selectedContacts: List<ContactInfo>,
    selectedFiles: List<FileInfo>,
    selectedVideos: List<VideoInfo>,
    selectedPhotos: List<PhotoInfo>,
    selectedSongs: List<SongInfo>
) {
    val groupedItems = listOfNotNull(
        "Apps" to selectedApps.takeIf { it.isNotEmpty() },
        "Files" to selectedFiles.takeIf { it.isNotEmpty() },
        "Videos" to selectedVideos.takeIf { it.isNotEmpty() },
        "Photos" to selectedPhotos.takeIf { it.isNotEmpty() },
        "Songs" to selectedSongs.takeIf { it.isNotEmpty() },
        "Contacts" to selectedContacts.takeIf { it.isNotEmpty() }
    )

    if (groupedItems.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No items selected", color = Color.Gray)
        }
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        groupedItems.forEach { (category, items) ->
            item {
                Text(
                    text = category,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    fontWeight = FontWeight.Bold
                )
            }
            items(items!!) { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    val icon = when (item) {
                        is AppInfo -> item.icon
                        is ContactInfo -> item.icon
                        is FileInfo -> item.icon
                        is VideoInfo -> item.icon
                        is PhotoInfo -> item.icon
                        is SongInfo -> item.icon
                    }

                    if (icon != null) {
                        Image(
                            painter = icon,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    } else {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.InsertDriveFile,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        val title = when (item) {
                            is AppInfo -> item.name
                            is ContactInfo -> item.name
                            is FileInfo -> item.name
                            is VideoInfo -> item.name
                            is PhotoInfo -> item.name
                            is SongInfo -> item.name
                        }
                        val subtitle = when (item) {
                            is ContactInfo -> item.phone
                            is VideoInfo -> "${item.size} · ${item.duration}"
                            is SongInfo -> "${item.size} · ${item.duration}"
                            is PhotoInfo -> "${item.size} · ${item.date}"
                            else -> (item as? AppInfo)?.size ?: (item as? FileInfo)?.size ?: ""
                        }

                        Text(text = title, fontWeight = FontWeight.Medium)
                        Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}*/
