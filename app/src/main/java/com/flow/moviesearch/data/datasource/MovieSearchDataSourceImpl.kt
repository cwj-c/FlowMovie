package com.flow.moviesearch.data.datasource

import com.flow.moviesearch.data.entity.response.MovieSearchResponse
import com.flow.moviesearch.data.service.MovieSearchService
import com.flow.moviesearch.presentation.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieSearchDataSourceImpl @Inject constructor(
    private val searchService: MovieSearchService,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : MovieSearchDataSource {

    override suspend fun searchMovie(query: String, start: Int): MovieSearchResponse {
        return withContext(dispatcher) {
            searchService.searchMovie(query, start)
        }
    }

}