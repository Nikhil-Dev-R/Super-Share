package com.rudraksha.supershare.core.viewmodel

import androidx.lifecycle.ViewModel
import com.rudraksha.supershare.core.domain.model.FileTransfer
import com.rudraksha.supershare.core.domain.model.NetworkMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class TransferViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TransferUiState())
    val uiState: StateFlow<TransferUiState> = _uiState

    /** Add a file to transfer queue */
    fun addFile(file: FileTransfer) {
        _uiState.update { current ->
            current.copy(files = current.files + file)
        }
    }

    /** Update progress for a specific file by name (you can improve this with unique IDs) */
    fun updateProgress(fileName: String, transferredSize: String, progress: Float) {
        _uiState.update { current ->
            val updatedFiles = current.files.map {
                if (it.fileName == fileName) {
                    it.copy(
                        transferredSize = transferredSize,
                        progress = progress,
                        isInProgress = progress < 1.0f
                    )
                } else it
            }
            current.copy(files = updatedFiles)
        }
    }

    /** Mark a file as complete manually (for error recovery or test purposes) */
    fun markComplete(fileName: String) {
        _uiState.update { current ->
            val updatedFiles = current.files.map {
                if (it.fileName == fileName) {
                    it.copy(progress = 1.0f, transferredSize = it.totalSize, isInProgress = false)
                } else it
            }
            current.copy(files = updatedFiles)
        }
    }

    /** Remove a file from the list (e.g. cancelled or failed transfer) */
    fun removeFile(fileName: String) {
        _uiState.update { current ->
            current.copy(files = current.files.filterNot { it.fileName == fileName })
        }
    }

    fun cancelFile(fileName: String) {
        _uiState.update { current ->
            val updatedFiles = current.files.map {
                if (it.fileName == fileName) {
                    it.copy(isInProgress = false)
                } else it
            }
            current.copy(files = updatedFiles)
        }
    }

    /** Cancel all ongoing transfers */
    fun cancelAll() {
        _uiState.update { current ->
            current.copy(files = emptyList())
        }
    }

    /** Reset the session to initial state */
    fun resetSession(peerName: String = "", connectionType: NetworkMode? = null) {
        _uiState.value = TransferUiState(peerName = peerName, connectionType = connectionType)
    }
}

data class TransferUiState(
    val peerName: String = "",
    val connectionType: NetworkMode? = null,
    val files: List<FileTransfer> = emptyList()
)
