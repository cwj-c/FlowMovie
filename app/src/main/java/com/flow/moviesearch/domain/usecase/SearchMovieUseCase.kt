package com.flow.moviesearch.domain.usecase

import com.flow.moviesearch.domain.model.MovieSearchResModel
import com.flow.moviesearch.domain.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SearchMovieUseCase(
    private val repository: MovieRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(query: String, page: Int): Result<MovieSearchResModel> {
        return kotlin.runCatching {
            withContext(dispatcher) {
                repository.searchMovie(query, page)
            }
        }.also {
            if(it.isSuccess) {
                kotlin.runCatching {
                    withContext(dispatcher) {
                        repository.saveRecentQuery(query)
                    }
                }
            }
        }
    }
}