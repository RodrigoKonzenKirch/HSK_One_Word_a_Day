package com.example.hskonewordaday.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hskonewordaday.di.DispatcherIo
import com.example.hskonewordaday.domain.WordsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: WordsRepository,
    @DispatcherIo private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState: MutableStateFlow<MainScreenUiState> = MutableStateFlow(MainScreenUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadAllWords()
    }

    private fun loadAllWords() {
        viewModelScope.launch(ioDispatcher) {
            _uiState.value = MainScreenUiState.Loading

            try {
                repository.getAllWords().collectLatest { words ->
                    if (words.isEmpty()) {
                        _uiState.value = MainScreenUiState.Empty
                    } else {
                        _uiState.value = MainScreenUiState.Success(words)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = MainScreenUiState.Error("Error loading words")

                e.printStackTrace()
            }

        }
    }

}