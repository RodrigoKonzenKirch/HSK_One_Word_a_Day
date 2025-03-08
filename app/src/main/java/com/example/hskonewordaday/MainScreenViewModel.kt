package com.example.hskonewordaday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hskonewordaday.data.ChineseWordEntity
import com.example.hskonewordaday.di.DispatcherIo
import com.example.hskonewordaday.domain.WordsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: WordsRepository,
    @DispatcherIo private val ioDispatcher: CoroutineDispatcher

) : ViewModel() {

    private val _allWords = MutableStateFlow<List<ChineseWordEntity>>(emptyList())
    val allWords = _allWords.asStateFlow()

    init {
        getAllWords()
    }

    private fun getAllWords() {
        viewModelScope.launch(ioDispatcher) {
            repository.getAllWords().collect {
                _allWords.value = it
            }
        }
    }

}