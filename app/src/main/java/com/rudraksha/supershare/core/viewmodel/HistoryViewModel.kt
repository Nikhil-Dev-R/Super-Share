package com.rudraksha.supershare.core.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rudraksha.supershare.core.domain.repository.PickerRepository
import com.rudraksha.supershare.core.domain.repository.PickerRepositoryImpl
import com.rudraksha.supershare.core.ui.screens.history.HistoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HistoryUiState(
    val history: List<HistoryItem> = emptyList()
)

class HistoryViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val repository: PickerRepository = PickerRepositoryImpl(application)

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _uiState.update { it.copy(history = repository.getHistory().toMutableList()) }
        }
    }

    fun toggleHistory(item: HistoryItem) {
        _uiState.update {
            val updatedSelection = it.history.toMutableList().apply {
                if (contains(item)) remove(item) else add(item)
            }
            it.copy(history = updatedSelection)
        }
    }
}