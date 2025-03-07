package com.example.hskonewordaday.data

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class WordsRepositoryImplTest {

    private val wordsDao = mockk<ChineseWordDao>(relaxed = true)
    private val repository = WordsRepositoryImpl(wordsDao)

    private val randomWordTest = ChineseWordEntity(0, "hsk1", "一", "一", "yi", "yi1", "one")
    private val allWordsTest = listOf(
        ChineseWordEntity(1, "hsk1", "一", "一", "yi", "yi1", "one"),
        ChineseWordEntity(2, "hsk1", "二", "二", "er", "er2", "two"),
        ChineseWordEntity(3, "hsk1", "三", "三", "san", "san3", "three")
    )

    @Test
    fun `getRandomWord success`() = runTest {

        coEvery { wordsDao.getRandomWord() } returns randomWordTest

        val result = repository.getRandomWord()
        assertThat(result).isEqualTo(randomWordTest)
    }

    @Test
    fun `getAllWords success`() = runTest {

        coEvery { wordsDao.getAllWords() } returns flowOf(allWordsTest)

        val result = repository.getAllWords()
        assertThat(result.first()).isEqualTo(allWordsTest)
    }

}