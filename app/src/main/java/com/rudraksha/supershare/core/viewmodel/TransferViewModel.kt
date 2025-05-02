package com.rudraksha.supershare.core.viewmodel

import androidx.lifecycle.ViewModel
import com.rudraksha.supershare.core.domain.model.FileTransfer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class TransferViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        TransferUiState(
            peerName = "Redmi Note 12",
            connectionType = "Hotspot",
            files = listOf(
                FileTransfer("video.mp4", "500MB", "200MB", 0.4f, true),
                FileTransfer("photo.jpg", "3MB", "3MB", 1.0f, false),
                FileTransfer("document.pdf", "10MB", "4MB", 0.4f, true)
            )
        )
    )
    val uiState: StateFlow<TransferUiState> = _uiState

    // Add logic for updating progress, adding files, canceling, etc.
}

data class TransferUiState(
    val peerName: String = "Unknown Device",
    val connectionType: String = "Hotspot",
    val files: List<FileTransfer> = emptyList()
)

