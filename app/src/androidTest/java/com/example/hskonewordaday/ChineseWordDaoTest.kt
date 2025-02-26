package com.example.hskonewordaday

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.hskonewordaday.data.AppDatabase
import com.example.hskonewordaday.data.ChineseWordDao
import com.example.hskonewordaday.data.ChineseWordEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import com.google.common.truth.Truth.assertThat


@RunWith(AndroidJUnit4::class)
class ChineseWordDaoTest {
    private lateinit var chineseWordDao: ChineseWordDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        chineseWordDao = db.chineseWordDao()

    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        db.close()
    }

    @Test
    fun insertAndGetWord() = runTest {
        val word = ChineseWordEntity(1, "一", "一", "yi", "yi1", "one")
        chineseWordDao.insert(word)
        val words = chineseWordDao.getAllWords()
        assertThat(words.first()).contains(word)

    }

    @Test
    fun insertAndGetListOfWords() = runTest {

        val words = listOf(
            ChineseWordEntity(1, "一", "一", "yi", "yi1", "one"),
            ChineseWordEntity(2, "二", "二", "er", "er2", "two"),
            ChineseWordEntity(3, "三", "三", "san", "san3", "three")
        )
        chineseWordDao.insertAll(words)
        val wordsFromDb = chineseWordDao.getAllWords()
        assertThat(wordsFromDb.first()).containsExactlyElementsIn(words)
        assertThat(wordsFromDb.first()).hasSize(words.size)

    }

    @Test
    fun insertAndGetRandomWord() = runTest {
        val words = listOf(
            ChineseWordEntity(1, "一", "一", "yi", "yi1", "one"),
            ChineseWordEntity(2, "二", "二", "er", "er2", "two"),
            ChineseWordEntity(3, "三", "三", "san", "san3", "three")
        )
        chineseWordDao.insertAll(words)
        val randomWord = chineseWordDao.getRandomWord()

        assertThat(randomWord.first()).isIn(words)
    }




}