package com.flow.moviesearch.data.service

import com.flow.moviesearch.data.constant.NetworkConstant
import com.flow.moviesearch.data.entity.response.MovieSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieSearchService {
    @GET("movie.json")
    suspend fun searchMovie(
        @Query("query")query: String,
        @Query("start")start: Int,
        @Query("display")display: Int = NetworkConstant.RESPONSE_DISPLAY
    ): MovieSearchResponse
}