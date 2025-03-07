package com.example.hskonewordaday.di

import com.example.hskonewordaday.data.WordsRepositoryImpl
import com.example.hskonewordaday.domain.WordsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindRepository(repositoryImpl: WordsRepositoryImpl): WordsRepository

}