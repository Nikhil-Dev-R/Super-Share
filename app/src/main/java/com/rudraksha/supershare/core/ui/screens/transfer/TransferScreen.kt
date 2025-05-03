package com.rudraksha.supershare.core.ui.screens.transfer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rudraksha.supershare.core.domain.model.FileTransfer
import com.rudraksha.supershare.core.viewmodel.TransferViewModel

@Composable
fun TransferScreen(viewModel: TransferViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Transfer In Progress",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        ConnectionInfo(
            peerName = uiState.peerName,
            connectionType = uiState.connectionType
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Files", style = MaterialTheme.typography.titleMedium)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            items(uiState.files) { file ->
                FileTransferItem(file)
            }
        }
    }
}

@Composable
fun ConnectionInfo(peerName: String, connectionType: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.WifiTethering,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Connected to", style = MaterialTheme.typography.labelMedium)
                Text(peerName, style = MaterialTheme.typography.bodyLarge)
                Text(connectionType, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
fun FileTransferItem(file: FileTransfer) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FileCopy,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = file.fileName,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                LinearProgressIndicator(
                    progress = {file.progress},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .padding(top = 4.dp)
                )
                Text(
                    text = "${(file.progress * 100).toInt()}% • ${file.transferredSize}/${file.totalSize}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            if (file.inProgress) {
                IconButton(onClick = { /* Cancel action */ }) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Cancel"
                    )
                }
            }
        }
    }
}
