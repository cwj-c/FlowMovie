package com.flow.moviesearch.presentation.di

import com.flow.moviesearch.data.service.MovieSearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    fun provideMovieSearchService(retrofit: Retrofit): MovieSearchService =
        retrofit.create(MovieSearchService::class.java)
}