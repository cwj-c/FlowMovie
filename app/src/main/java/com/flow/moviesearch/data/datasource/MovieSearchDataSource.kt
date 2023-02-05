package com.flow.moviesearch.data.datasource

import com.flow.moviesearch.data.entity.response.MovieSearchResponse

interface MovieSearchDataSource {
    suspend fun searchMovie(query: String, start: Int): MovieSearchResponse
}