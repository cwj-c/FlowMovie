package com.flow.moviesearch.domain.repository

import com.flow.moviesearch.domain.model.DomainException
import com.flow.moviesearch.domain.model.MovieSearchResModel

interface MovieRepository {

    @kotlin.jvm.Throws(DomainException.MaxPageException::class)
    suspend fun searchMovie(query: String, page: Int): MovieSearchResModel

    suspend fun saveRecentQuery(query: String): Boolean

    suspend fun fetchRecentQuery(): List<String>

}