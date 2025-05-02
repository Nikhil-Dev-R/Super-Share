package com.rudraksha.supershare.core.ui.screens.filemanager

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun <T> MediaTab(
    items: List<T>,
    selected: List<T>,
    iconExtractor: (T) -> Painter?,
    titleExtractor: (T) -> String,
    subtitleExtractor: (T) -> String,
    onToggle: (T) -> Unit
) {
    LazyColumn {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle(item) }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val icon = iconExtractor(item)
                icon?.let {
                    Icon(painter = it, contentDescription = null)
                } ?: Icon(Icons.Default.Image, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(titleExtractor(item), fontWeight = FontWeight.Bold)
                    Text(subtitleExtractor(item), fontSize = 12.sp, color = Color.Gray)
                }
                Checkbox(
                    checked = selected.contains(item),
                    onCheckedChange = { onToggle(item) }
                )
            }
        }
    }
}

@Composable fun VideoTab(videos: List<VideoInfo>, selected: List<VideoInfo>, onToggle: (VideoInfo) -> Unit) =
    MediaTab(videos, selected, { it.icon }, { it.name }, { it.duration }, onToggle)

@Composable fun PhotoTab(photos: List<PhotoInfo>, selected: List<PhotoInfo>, onToggle: (PhotoInfo) -> Unit) =
    MediaTab(photos, selected, { it.icon }, { it.name }, { it.date }, onToggle)

@Composable fun SongTab(songs: List<SongInfo>, selected: List<SongInfo>, onToggle: (SongInfo) -> Unit) =
    MediaTab(songs, selected, { it.icon }, { it.name }, { it.duration }, onToggle)
