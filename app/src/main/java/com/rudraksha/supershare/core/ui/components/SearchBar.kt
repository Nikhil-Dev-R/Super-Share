package com.rudraksha.supershare.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun SuperShareSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = "Search...",
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(placeholder) },
        singleLine = true,
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search Icon")
        },
        modifier = modifier
            .fillMaxWidth()
//            .height(48.dp)
            .clip(RoundedCornerShape(12.dp)),
    )
}
