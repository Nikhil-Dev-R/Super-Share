package com.rudraksha.supershare.core.ui.screens.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rudraksha.supershare.R
import com.rudraksha.supershare.ui.theme.SuperYellow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope
import kotlin.String

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSendClick: () -> Unit,
    onReceiveClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onPCConnectClick: () -> Unit,
    onSettingsClick: () -> Unit,
    grantedPermissionsFlow: StateFlow<Set<String>>,
    requiredPermissions: Set<String>,
    hasPermissions: (Set<String>) -> Boolean,
    updatePermissionsStatus: (Set<String>) -> Unit,
    openAppSettings: (Context) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )

    // Show sheet if permissions are not granted
    LaunchedEffect(Unit) {
        if (!hasPermissions(requiredPermissions)) {
            coroutineScope.launch {
                sheetState.bottomSheetState.expand()
            }
        }
    }

    BottomSheetScaffold(
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                PermissionBottomSheet(
                    grantedPermissionsFlow = grantedPermissionsFlow,
                    requiredPermissions = requiredPermissions,
                    updatePermissionsStatus = updatePermissionsStatus,
                    hasPermissions = hasPermissions,
                    onAllPermissionsGranted = {
                        coroutineScope.launch {
                            sheetState.bottomSheetState.hide()
                        }
                    },
                    openAppSettings = openAppSettings
                )
            }
        },
        scaffoldState = sheetState,
        sheetPeekHeight = 0.dp,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background
            Image(
                painter = painterResource(id = R.drawable.home_design),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )

            // Foreground content (Scaffold and Buttons)
            Scaffold(
                containerColor = Color.Transparent,
                contentColor = Color.White,
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BottomAppBar(
                        containerColor = Color.Transparent,
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BottomActionButton(
                                imageVector = Icons.Filled.History,
                                label = "History",
                                onClick = onHistoryClick,
                                modifier = Modifier.weight(1f)
                            )

                            BottomActionButton(
                                imageVector = Icons.Filled.Monitor,
                                label = "Connect to PC",
                                onClick = {
                                    coroutineScope.launch {
                                        checkOnClick(
                                            sheetState = sheetState.bottomSheetState,
                                            context = context,
                                            requiredPermissions = requiredPermissions,
                                            hasPermissions = hasPermissions,
                                            onClick = onPCConnectClick
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                },
            ) { padding ->
                // Main content (Send/Receive Buttons)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        ActionButtonCard(
                            iconRes = R.drawable.send,
                            label = "Send",
                            onClick = {
                                coroutineScope.launch {
                                    checkOnClick(
                                        sheetState = sheetState.bottomSheetState,
                                        context = context,
                                        requiredPermissions = requiredPermissions,
                                        hasPermissions = hasPermissions,
                                        onClick = onSendClick
                                    )
                                }
                            },
                            shape = RoundedCornerShape(
                                topStartPercent = 0,
                                topEndPercent = 50,
                                bottomStartPercent = 0,
                                bottomEndPercent = 50
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = onSettingsClick,
                            modifier = Modifier
                                .weight(0.5f)
                                .aspectRatio(1f)
                                .padding(20.dp)
                                .clip(RoundedCornerShape(percent = 50))
                                .background(MaterialTheme.colorScheme.secondary),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Settings",
                                modifier = Modifier.fillMaxSize(0.6f)
                            )
                        }

                        ActionButtonCard(
                            iconRes = R.drawable.receive,
                            label = "Receive",
                            onClick = {
                                coroutineScope.launch {
                                    checkOnClick(
                                        sheetState = sheetState.bottomSheetState,
                                        context = context,
                                        requiredPermissions = requiredPermissions,
                                        hasPermissions = hasPermissions,
                                        onClick = onReceiveClick
                                    )
                                }
                            },
                            shape = RoundedCornerShape(
                                topStartPercent = 50,
                                topEndPercent = 0,
                                bottomStartPercent = 50,
                                bottomEndPercent = 0
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActionButtonCard(
    iconRes: Int,
    label: String,
    onClick: () -> Unit,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            onClick = onClick,
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(percent = 50),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "Receive",
                modifier = Modifier.fillMaxSize()
            )
        }
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = shape
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun BottomActionButton(
    imageVector: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxHeight()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = label,
            tint = SuperYellow
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = SuperYellow,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
//@Composable
suspend fun checkOnClick(
    sheetState: SheetState,
    context: Context,
    requiredPermissions: Set<String>,
    hasPermissions: (Set<String>) -> Boolean,
    onClick: () -> Unit
) {
    if (hasPermissions(requiredPermissions)) {
        onClick()
    } else {
        Toast.makeText(
            context,
            "Please grant permissions first.",
            Toast.LENGTH_SHORT
        ).show()
        sheetState.expand()
    }
}