package com.rudraksha.supershare.core.ui.screens.pcconnect

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NetworkWifi
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rudraksha.supershare.core.domain.model.NetworkMode
import com.rudraksha.supershare.core.ui.components.SuperShareTopBar

@Preview
@Composable
fun TransmissionModeScreen(
    onModeSelected: (NetworkMode) -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            SuperShareTopBar(
                title = "Transmission Mode",
                showBackButton = true,
                onBackClick = onBackClick,
                centerTitle = false
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {
            ModeCard(
                title = "Hotspot Mode",
                subtitle = "The other device connects to the phone's hotspot. No internet data usage.",
                tag = "Faster",
                color = Color(0xFF4CAF50),
                icon = Icons.Default.NetworkWifi,
                onClick = {
                    onModeSelected(NetworkMode.Hotspot)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ModeCard(
                title = "Wi-Fi Mode",
                subtitle = "Phone and the other device connect to the same Wi-Fi.",
                tag = "Convenient",
                color = Color(0xFF03A9F4),
                icon = Icons.Default.Wifi,
                onClick = {
                    onModeSelected(NetworkMode.WiFi)
                }
            )
        }
    }
}

@Composable
fun ModeCard(
    title: String,
    subtitle: String,
    tag: String,
    color: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card (
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray
                )
            }

            Box(
                modifier = Modifier
                    .background(Color(0xFFFFF59D), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = tag,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
