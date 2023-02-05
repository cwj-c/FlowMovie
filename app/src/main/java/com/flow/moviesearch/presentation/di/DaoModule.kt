package com.flow.moviesearch.presentation.di

import com.flow.moviesearch.data.dao.RecentQueryDao
import com.flow.moviesearch.data.local.MovieRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideRecentQueryDao(db: MovieRoomDatabase): RecentQueryDao = db.recentQueryDao()
}