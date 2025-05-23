package com.rudraksha.supershare.core.ui.screens.filemanager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rudraksha.supershare.core.domain.model.FileItem

@Composable
fun FileTab(
    files: List<FileItem>,
    selected: List<FileItem>,
    onToggle: (FileItem) -> Unit
) {
    LazyColumn {
        items(files) { file ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle(file) }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
//                file.icon?.let {
//                    Icon(painter = it, contentDescription = null)
//                } ?: Icon(Icons.AutoMirrored.Default.InsertDriveFile, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(file.name, fontWeight = FontWeight.Bold)
                    Text(file.size.toString(), fontSize = 12.sp, color = Color.Gray)
                }
                Checkbox(
                    checked = selected.contains(file),
                    onCheckedChange = { onToggle(file) }
                )
            }
        }
    }
}
