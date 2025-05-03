package com.rudraksha.supershare.core.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rudraksha.supershare.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSendClick: () -> Unit,
    onReceiveClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onPCConnectClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Transparent,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(vertical = 8.dp)
                            .weight(1f)
                            .clickable {
                                onHistoryClick()
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.History,
                            contentDescription = "History",
                        )
                        Text(
                            text = "History",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(vertical = 8.dp)
                            .weight(1f)
                            .clickable {
                                onPCConnectClick()
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Monitor,
                            contentDescription = "PC",
                        )
                        Text(
                            text = "Connect to PC",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.home_design),
                contentDescription = "Home Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        onClick = onSendClick,
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(percent = 50),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.send),
                            contentDescription = "Send",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Button(
                        onClick = onSendClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(
                            topStartPercent = 0,
                            topEndPercent = 50,
                            bottomStartPercent = 0,
                            bottomEndPercent = 50
                        )
                    ) {
                        Text(
                            text = "Send",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .aspectRatio(1f)
                        .padding(16.dp)
                        .clickable{
                            onSettingsClick
                        }
                        .clip(RoundedCornerShape(percent = 50))
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier.fillMaxSize(0.8f)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        onClick = onReceiveClick,
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(percent = 50),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.receive),
                            contentDescription = "Receive",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Button(
                        onClick = onReceiveClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(
                            topStartPercent = 50,
                            topEndPercent = 0,
                            bottomStartPercent = 50,
                            bottomEndPercent = 0
                        )
                    ) {
                        Text(
                            text = "Receive",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
