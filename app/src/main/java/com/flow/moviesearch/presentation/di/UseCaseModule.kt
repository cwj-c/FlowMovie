package com.flow.moviesearch.presentation.di

import com.flow.moviesearch.domain.repository.MovieRepository
import com.flow.moviesearch.domain.usecase.FetchRecentQueryUseCase
import com.flow.moviesearch.domain.usecase.SearchMovieUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher


@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideFetchRecentQueryUseCase(
        repository: MovieRepository,
        @DefaultDispatcher dispatcher: CoroutineDispatcher
    ) = FetchRecentQueryUseCase(repository, dispatcher)

    @Provides
    fun provideSearchMovieUseCase(
        repository: MovieRepository,
        @DefaultDispatcher dispatcher: CoroutineDispatcher
    ) = SearchMovieUseCase(repository, dispatcher)
}