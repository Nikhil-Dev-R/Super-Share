package com.rudraksha.supershare.core.ui.screens.pcconnect

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rudraksha.supershare.core.domain.model.NetworkMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectToReceiverScreen(networkMode: NetworkMode) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        TopAppBar(
            title = { Text("Connect to Receiver", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = { /* handle back */ }) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = null, tint = Color.White)
                }
            },
            actions = {
                TextButton(onClick = { /* handle send to PC */ }) {
                    Text("Send to PC", color = Color.White)
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Black)
        )

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            // Placeholder for QR Code scanner
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(250.dp)
                    .border(2.dp, Color.Green, RoundedCornerShape(8.dp))
            ) {
                Text(
                    "Place the QR Code in the frame",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp)
                )
            }

            Column(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color.Green, modifier = Modifier.size(36.dp))
                Text("Searching for receiver...", color = Color.White, modifier = Modifier.padding(top = 8.dp))
                Text("No receiver found, you can scan QR Code to connect.", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}
