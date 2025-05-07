package com.rudraksha.supershare.core.ui.screens.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HistoryItemView(item: HistoryItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (item.icon != null) {
            Image(
                painter = item.icon,
                contentDescription = item.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Default.InsertDriveFile,
                contentDescription = item.name,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.name, fontWeight = FontWeight.Medium)
            Text(
                text = "${item.type} · ${item.size}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = item.date.toString(),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        val directionIcon = when (item.direction) {
            TransferDirection.SENT -> Icons.Default.NorthEast
            TransferDirection.RECEIVED -> Icons.Default.SouthWest
        }

        Icon(
            imageVector = directionIcon,
            contentDescription = item.direction.name,
            tint = if (item.direction == TransferDirection.SENT)
                MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun HistoryListItem(
    item: HistoryItem,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (item.icon != null) {
            Image(
                painter = item.icon,
                contentDescription = item.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Default.InsertDriveFile,
                contentDescription = item.name,
                modifier = Modifier.size(48.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f).padding(start = 12.dp)
        ) {
            Text(item.name, style = MaterialTheme.typography.bodyLarge)
            Text(item.size, style = MaterialTheme.typography.bodySmall)
        }
        Checkbox(checked = isSelected, onCheckedChange = { onToggle() })
    }
}
