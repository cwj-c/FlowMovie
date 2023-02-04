package com.flow.moviesearch.data.repository

import com.flow.moviesearch.data.constant.NetworkConstant
import com.flow.moviesearch.data.datasource.MovieSearchDataSource
import com.flow.moviesearch.domain.model.DomainException
import com.flow.moviesearch.domain.model.MovieSearchResModel
import com.flow.moviesearch.domain.repository.MovieRepository
import com.flow.moviesearch.presentation.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieSearchDataSource: MovieSearchDataSource,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : MovieRepository {

    private var total = 0

    override suspend fun searchMovie(query: String, page: Int): MovieSearchResModel {
        return withContext(dispatcher) {
            when (page) {
                0, 1 -> {
                    movieSearchDataSource.searchMovie(query, 1)
                        .also { total = it.total }
                        .toModel(1)
                }
                else -> {
                    val totalPage = getTotalPage(total)
                    if(totalPage < page) {
                        throw DomainException.MaxPageException("total page is $totalPage, current page is $page")
                    }
                    val start = (page-1) * NetworkConstant.RESPONSE_DISPLAY + 1 // 이전 페이지 마지막 Item + 1
                    movieSearchDataSource.searchMovie(query, start)
                        .also { total = it.total }
                        .toModel(page)
                }
            }
        }
    }

    private fun getTotalPage(total: Int, display: Int = NetworkConstant.RESPONSE_DISPLAY): Int {
        return when (total % display == 0) {
            true -> total/display
            else -> total/display + 1
        }
    }

    override suspend fun saveRecentQuery(query: String): Boolean {
        return true
    }

    override suspend fun fetchRecentQuery(): List<String> {
        return emptyList()
    }

}