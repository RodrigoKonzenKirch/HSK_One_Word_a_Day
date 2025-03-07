package com.example.hskonewordaday.domain

import com.example.hskonewordaday.data.ChineseWordEntity
import kotlinx.coroutines.flow.Flow

interface WordsRepository {

    suspend fun getRandomWord(): ChineseWordEntity

    suspend fun getAllWords(): Flow<List<ChineseWordEntity>>


}