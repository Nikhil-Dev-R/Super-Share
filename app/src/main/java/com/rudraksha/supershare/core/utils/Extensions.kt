package com.rudraksha.supershare.core.utils

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.core.graphics.createBitmap
import com.rudraksha.supershare.core.ui.screens.filemanager.ShareableItem
import com.rudraksha.supershare.core.ui.screens.history.HistoryItem
import com.rudraksha.supershare.core.ui.screens.history.TransferDirection

fun Drawable.toPainter(): Painter {
    val width = if (intrinsicWidth > 0) intrinsicWidth else 1
    val height = if (intrinsicHeight > 0) intrinsicHeight else 1

    val bitmap = createBitmap(width, height)
    val canvas = Canvas(bitmap)

    setBounds(0, 0, width, height)
    draw(canvas)

    return BitmapPainter(bitmap.asImageBitmap())
}

fun ShareableItem.toHistoryItem(): HistoryItem {
    return HistoryItem(
        id = name,
        name = name,
        type = this::class.simpleName ?: "Unknown",
        size = size.toString(),
        date = "", // You can inject current timestamp if needed
        icon = icon,
        direction = TransferDirection.SENT,
        isSelected = isSelected
    )
}
