package com.example.hskonewordaday.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chinese_words")
data class ChineseWordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "chinese_simplified") val chineseSimplified: String,
    @ColumnInfo(name = "chinese_traditional") val chineseTraditional: String,
    @ColumnInfo(name = "pronunciation_number") val pronunciationNumber: String,
    @ColumnInfo(name = "pronunciation_symbol") val pronunciationSymbol: String,
    @ColumnInfo(name = "meaning") val meaning: String
)
