package com.rudraksha.supershare.core.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rudraksha.supershare.core.domain.model.AppItem
import com.rudraksha.supershare.core.domain.model.AudioItem
import com.rudraksha.supershare.core.domain.model.ContactItem
import com.rudraksha.supershare.core.domain.model.FileItem
import com.rudraksha.supershare.core.domain.model.MediaItem
import com.rudraksha.supershare.core.domain.model.ShareableItem
import com.rudraksha.supershare.core.domain.model.ShareableType
import com.rudraksha.supershare.core.domain.repository.PickerRepository
import com.rudraksha.supershare.core.domain.repository.PickerRepositoryImpl
import com.rudraksha.supershare.core.ui.screens.history.HistoryItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PickerUiState(
    val selectedItems: Set<ShareableItem> = emptySet(),
    val apps: List<AppItem> = emptyList(),
    val files: List<FileItem> = emptyList(),
    val videos: List<MediaItem> = emptyList(),
    val photos: List<MediaItem> = emptyList(),
    val songs: List<AudioItem> = emptyList(),
    val contacts: List<ContactItem> = emptyList(),
    val history: MutableList<HistoryItem> = mutableListOf()
)

sealed class PickerEvent {
    object ProceedToNext : PickerEvent()
    data class ShowError(val message: String) : PickerEvent()
}

class PickerScreenViewModel(
//    private val repository: PickerRepository,
    application: Application,
) : AndroidViewModel(application) {

    private val repository: PickerRepository = PickerRepositoryImpl(application)

    private val _uiState = MutableStateFlow(PickerUiState())
    val uiState: StateFlow<PickerUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<PickerEvent>()
    val event: SharedFlow<PickerEvent> = _event.asSharedFlow()

    init {
        loadAllTabs()
    }

    private fun loadAllTabs() {
        viewModelScope.launch {
            loadApps()
            loadFiles()
            loadVideos()
            loadPhotos()
            loadSongs()
            loadContacts()
            loadHistory()
        }
    }

    private fun loadApps() {
//        val dummyApps = List(10) { index ->
//            AppItem(packageName = "com.app.$index", name = "App $index", icon = null)
//        }
//        _uiState.update { it.copy(apps = dummyApps) }
        viewModelScope.launch {
            _uiState.update { it.copy(apps = repository.getInstalledApps()) }
        }
    }

    private fun loadFiles() {
//        val dummyFiles = List(5) {
//            FileItem(
//                name = "File_$it.pdf",
//                path = "/storage/emulated/0/File_$it.pdf",
//                size = 12345L
//            )
//        }
//        _uiState.update { it.copy(files = dummyFiles) }
        viewModelScope.launch {
            _uiState.update { it.copy(files = repository.getFiles()) }
        }
    }

    private fun loadVideos() {
//        val dummyVideos = List(8) {
//            MediaItem(
//                name = "Video_$it.mp4",
//                uri = "content://video/$it",
//                size = 23456L,
//                duration = 100000,
//                mediaType = ShareableType.VIDEO
//            )
//        }
//        _uiState.update { it.copy(videos = dummyVideos) }
        viewModelScope.launch {
            _uiState.update { it.copy(videos = repository.getVideos()) }
        }

    }

    private fun loadPhotos() {
//        val dummyPhotos = List(12) {
//            MediaItem(
//                name = "Photo_$it.jpg", uri = "content://image/$it",
//                size = 34567L, duration = 0,
//                mediaType = ShareableType.PHOTO
//            )
//        }
//        _uiState.update { it.copy(photos = dummyPhotos) }
        viewModelScope.launch {
            _uiState.update { it.copy(photos = repository.getPhotos()) }
        }
    }

    private fun loadSongs() {
//        val dummySongs = List(6) {
//            AudioItem(
//                name = "Song_$it.mp3",
//                uri = "content://audio/$it",
//                artist = "Artist $it",
//                size = 45678L
//            )
//        }
//        _uiState.update { it.copy(songs = dummySongs) }
        viewModelScope.launch {
            _uiState.update { it.copy(songs = repository.getSongs()) }
        }
    }

    private fun loadContacts() {
//        val dummyContacts = List(7) {
//            ContactItem(name = "Contact $it", number = "+9112345678$it")
//        }
//        _uiState.update { it.copy(contacts = dummyContacts) }
        viewModelScope.launch {
            _uiState.update { it.copy(contacts = repository.getContacts()) }
        }
    }

    private fun loadHistory() {
//        val dummyHistory = MutableList(4) {
//            HistoryItem(
//                id = "history_$it",
//                name = "History Item $it",
//                size = "10 MB",
//                type = ShareableType.FILE,
//                icon = null,
//                date = System.currentTimeMillis(),
//                isSelected = false
//            )
//        }
//        _uiState.update { it.copy(history = dummyHistory) }
        viewModelScope.launch {
            _uiState.update { it.copy(history = repository.getHistory().toMutableList()) }
        }
    }

    fun toggleItemSelection(item: ShareableItem) {
        _uiState.update {
            val updatedSelection = it.selectedItems.toMutableSet().apply {
                if (contains(item)) remove(item) else add(item)
            }
            it.copy(selectedItems = updatedSelection)
        }
    }
    fun toggleHistory(item: HistoryItem) {
        _uiState.update {
            val updatedSelection = it.history.apply {
                if (contains(item)) remove(item) else add(item)
            }
            it.copy(history = updatedSelection)
        }
    }

    fun isSelected(item: ShareableItem): Boolean {
        return _uiState.value.selectedItems.contains(item)
    }

    fun onNextClicked() {
        viewModelScope.launch {
            if (_uiState.value.selectedItems.isEmpty()) {
                _event.emit(PickerEvent.ShowError("Please select at least one item."))
            } else {
                _event.emit(PickerEvent.ProceedToNext)
            }
        }
    }
}
