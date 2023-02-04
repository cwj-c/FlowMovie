package com.flow.moviesearch.presentation.di

import com.flow.moviesearch.data.repository.MovieRepositoryImpl
import com.flow.moviesearch.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMovieRepository(impl: MovieRepositoryImpl): MovieRepository
}