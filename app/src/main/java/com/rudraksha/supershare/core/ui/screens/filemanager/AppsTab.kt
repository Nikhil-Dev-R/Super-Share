package com.rudraksha.supershare.core.ui.screens.filemanager

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rudraksha.supershare.core.domain.model.AppItem
import com.rudraksha.supershare.core.utils.toPainter

@Composable
fun AppsTab(
    apps: List<AppItem> = listOf(),
    selected: List<AppItem> = listOf(),
    onToggle: (AppItem) -> Unit = {}
) {
    LazyVerticalGrid(columns = GridCells.Fixed(4)) {
        items(apps) { app ->
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onToggle(app) },
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        app.icon?.let {
                            Image(
                                painter = it.toPainter(),
                                contentDescription = app.name,
                                modifier = Modifier.matchParentSize()
                            )
                        }
                        Checkbox(
                            checked = selected.contains(app),
                            onCheckedChange = { onToggle(app) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 4.dp, y = (-4).dp)
                        )
                    }
                    Text(
                        text = app.name,
                        maxLines = 1,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = app.size.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}