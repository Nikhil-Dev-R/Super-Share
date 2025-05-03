package com.rudraksha.supershare.core.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ActionIcon(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit = {},
    tint: Color = MaterialTheme.colorScheme.onPrimary
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}
