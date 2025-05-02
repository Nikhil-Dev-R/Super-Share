package com.rudraksha.supershare.core.ui.screens.filemanager

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomBar(selectedCount: Int, onNextClick: () -> Unit) {
    BottomAppBar {
        Text("$selectedCount Selected", modifier = Modifier
            .weight(1f)
            .padding(start = 16.dp))
        Button(
            onClick = onNextClick,
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Text("NEXT")
        }
    }
}