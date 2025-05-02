package com.rudraksha.supershare.core.ui.screens.filemanager

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rudraksha.supershare.R
import com.rudraksha.supershare.ui.theme.SuperShareTheme

data class FileItem(
    val id: String,
    val name: String,
    val size: String,
    val date: String,
    val isSelected: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileManagerScreen(
    category: String = "Images",
    files: List<FileItem>,
    onToggleSelect: (FileItem) -> Unit,
    onSendFiles: () -> Unit,
    isGridView: Boolean = true,
    onToggleViewType: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Select $category") },
            actions = {
                IconButton(onClick = onToggleViewType) {
                    Icon(
                        imageVector = if (isGridView) Icons.AutoMirrored.Filled.List else Icons.Filled.ViewModule,
                        contentDescription = "Toggle View"
                    )
                }
            }
        )

        if (isGridView) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.weight(1f).padding(8.dp),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(files) { file ->
                    FileGridItem(file = file, onToggleSelect = onToggleSelect)
                }
            }
        } else {
            Column(modifier = Modifier.weight(1f)) {
                files.forEach { file ->
                    FileListItem(file = file, onToggleSelect = onToggleSelect)
                }
            }
        }

        if (files.any { it.isSelected }) {
            BottomAppBar(
                actions = {
                    Text("${files.count { it.isSelected }} selected")
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = onSendFiles) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = "Send")
                    }
                }
            )
        }
    }
}

@Composable
fun FileGridItem(file: FileItem, onToggleSelect: (FileItem) -> Unit) {
    Column(
        modifier = Modifier
            .padding(6.dp)
            .clickable { onToggleSelect(file) }
            .background(
                if (file.isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                else MaterialTheme.colorScheme.surface
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.Folder,
            contentDescription = "File Icon",
            modifier = Modifier.size(48.dp),
            tint = if (file.isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = file.name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun FileListItem(file: FileItem, onToggleSelect: (FileItem) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleSelect(file) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Folder,
            contentDescription = "File Icon",
            modifier = Modifier.size(40.dp),
            tint = if (file.isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = file.name, style = MaterialTheme.typography.bodyMedium)
            Text(text = file.size, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FileManagerScreenPreview() {
    val sampleFiles = remember {
        List(10) {
            FileItem(
                id = it.toString(),
                name = "File$it.jpg",
                size = "${(1..100).random()} MB",
                date = "2024-01-01",
                isSelected = it % 2 == 0
            )
        }
    }
    SuperShareTheme {
        FileManagerScreen(
            files = sampleFiles,
            onToggleSelect = {},
            onSendFiles = {},
            onToggleViewType = {}
        )
    }
}
