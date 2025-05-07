package com.rudraksha.supershare.core.ui.screens.history

import androidx.compose.ui.graphics.painter.Painter
import com.rudraksha.supershare.core.domain.model.ShareableType
import kotlin.random.Random

enum class HistoryFilter { TYPE, SIZE, DATE }

data class HistoryItem(
    val id: String = "${Random.nextLong()}${System.currentTimeMillis()}",
    val name: String,
    val size: String,
    val type: ShareableType, // app, video, photo, etc.
    val icon: Painter?,
    val date: Long = System.currentTimeMillis(),
    val isSelected: Boolean = false,
    val direction: TransferDirection = TransferDirection.SENT
)

enum class TransferDirection {
    SENT, RECEIVED
}
