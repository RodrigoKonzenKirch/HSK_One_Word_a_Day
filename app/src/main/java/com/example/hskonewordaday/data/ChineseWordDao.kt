package com.example.hskonewordaday.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChineseWordDao {
    @Query("SELECT * FROM chinese_words")
    fun getAllWords(): Flow<List<ChineseWordEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: ChineseWordEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(words: List<ChineseWordEntity>)

    @Query("SELECT * FROM chinese_words ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomWord(): ChineseWordEntity

    @Query("SELECT COUNT(*) FROM chinese_words")
    suspend fun getWordCount(): Int

}