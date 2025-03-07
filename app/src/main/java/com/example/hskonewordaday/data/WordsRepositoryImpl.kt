package com.example.hskonewordaday.data

import com.example.hskonewordaday.domain.WordsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WordsRepositoryImpl @Inject constructor(
    private val wordsDao: ChineseWordDao
) : WordsRepository {

    override suspend fun getRandomWord(): ChineseWordEntity {
        return wordsDao.getRandomWord()
    }

    override suspend fun getAllWords(): Flow<List<ChineseWordEntity>> {
        return wordsDao.getAllWords()
    }
}