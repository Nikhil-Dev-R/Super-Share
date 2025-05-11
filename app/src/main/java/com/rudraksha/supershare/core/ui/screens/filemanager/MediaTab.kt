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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rudraksha.supershare.core.domain.model.AudioItem
import com.rudraksha.supershare.core.domain.model.MediaItem
import com.rudraksha.supershare.core.utils.toPainter

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
                    Image(
                        painter = it,
                        contentDescription = titleExtractor(item),
                        modifier = Modifier.size(64.dp)
                    )
                } ?: Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = titleExtractor(item),
                    modifier = Modifier.size(64.dp)
                )
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

@Composable
fun VideoTab(videos: List<MediaItem>, selected: List<MediaItem>, onToggle: (MediaItem) -> Unit) =
    MediaTab(
        videos,
        selected,
        { it.icon?.toPainter() },
        { it.name },
        { it.duration.toString() },
        onToggle
    )

@Composable
fun PhotoTab(photos: List<MediaItem>, selected: List<MediaItem>, onToggle: (MediaItem) -> Unit) =
    MediaTab(
        photos,
        selected,
        { it.icon?.toPainter() },
        { it.name },
        { it.date.toString() },
        onToggle
    )

@Composable
fun SongTab(songs: List<AudioItem>, selected: List<AudioItem>, onToggle: (AudioItem) -> Unit) =
    MediaTab(
        songs,
        selected,
        { it.icon?.toPainter() },
        { it.name },
        { it.size.toString() },
        onToggle
    )
