package com.flow.moviesearch.domain.usecase

import com.flow.moviesearch.domain.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FetchRecentQueryUseCase(
    private val repository: MovieRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): Result<List<String>> {
        return kotlin.runCatching {
            withContext(dispatcher) {
                repository.fetchRecentQuery()
            }
        }
    }
}