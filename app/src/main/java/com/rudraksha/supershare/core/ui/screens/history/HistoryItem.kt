package com.rudraksha.supershare.core.ui.screens.history

import androidx.compose.ui.graphics.painter.Painter
import kotlin.random.Random

enum class HistoryFilter { TYPE, SIZE, DATE }

data class HistoryItem(
    val id: String = "${Random.nextLong()}${System.currentTimeMillis()}",
    val name: String,
    val size: String,
    val type: String, // app, video, photo, etc.
    val date: String, // formatted as needed
    val icon: Painter?,
    val isSelected: Boolean = false,
    val direction: TransferDirection = TransferDirection.SENT
)

enum class TransferDirection {
    SENT, RECEIVED
}
