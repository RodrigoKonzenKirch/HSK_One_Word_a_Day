package com.example.hskonewordaday.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hskonewordaday.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

@Database(entities = [ChineseWordEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chineseWordDao(): ChineseWordDao

    companion object {
        const val DATABASE_NAME = "app_database"
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            populateDatabase(context)
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun populateDatabase(context: Context) {
            CoroutineScope(Dispatchers.IO).launch {
                val chineseWordDao = getDatabase(context).chineseWordDao()

                if (chineseWordDao.getWordCount() >= 0) {
                    val resourceId = R.raw.hsk1to6
                    val chineseWords = mutableListOf<ChineseWordEntity>()

                    val inputStream = context.resources.openRawResource(resourceId)
                    val reader = BufferedReader(InputStreamReader(inputStream))

                    reader.useLines { lines ->
                        lines.forEach { line ->
                            val columns = line.split('\t')
                            if (columns.size == 6) {
                                chineseWords.add(
                                    ChineseWordEntity(
                                        id = 0,
                                        hskLevel = columns[0],
                                        chineseSimplified = columns[1],
                                        chineseTraditional = columns[2],
                                        pronunciationNumber = columns[3],
                                        pronunciationSymbol = columns[4],
                                        meaning = columns[5]
                                    )
                                )
                            }
                        }
                    }
                    chineseWordDao.insertAll(chineseWords.toList())
                }
            }
        }

        fun destroyInstance() {
            INSTANCE = null
        }

    }

}