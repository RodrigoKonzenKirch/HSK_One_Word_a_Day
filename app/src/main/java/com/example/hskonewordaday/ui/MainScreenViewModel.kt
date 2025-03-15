package com.example.hskonewordaday.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hskonewordaday.data.ChineseWordEntity
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

    data class MainScreenState(
        val allWords: List<ChineseWordEntity> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(MainScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        loadAllWords()
    }

    private fun loadAllWords() {
        viewModelScope.launch(ioDispatcher) {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                repository.getAllWords().collectLatest { words ->
                    _uiState.value = _uiState.value.copy(allWords = words, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Error loading words")

                e.printStackTrace()
            }

//            repository.getAllWords().collect {
//                _uiState.value = it
//            }
        }
    }

}