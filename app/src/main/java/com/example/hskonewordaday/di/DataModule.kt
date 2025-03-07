package com.example.hskonewordaday.di

import android.content.Context
import com.example.hskonewordaday.data.AppDatabase
import com.example.hskonewordaday.data.ChineseWordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideWordDao(wordDatabase: AppDatabase): ChineseWordDao {
        return wordDatabase.chineseWordDao()
    }

}