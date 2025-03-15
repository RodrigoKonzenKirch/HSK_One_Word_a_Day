package com.example.hskonewordaday.ui

import com.example.hskonewordaday.data.ChineseWordEntity

sealed class MainScreenUiState {
    data object Loading : MainScreenUiState()
    data class Error(val message: String) : MainScreenUiState()
    data object Empty : MainScreenUiState()
    data class Success(val words: List<ChineseWordEntity>) : MainScreenUiState()
}