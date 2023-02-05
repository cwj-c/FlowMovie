package com.flow.moviesearch.presentation.di

import android.app.Application
import android.content.Context
import com.flow.moviesearch.data.local.MovieRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @ApplicationContext
    @Provides
    @Singleton
    fun provideApplicationContext(application: Application) = application

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context) = MovieRoomDatabase.getDatabase(context)
    
}