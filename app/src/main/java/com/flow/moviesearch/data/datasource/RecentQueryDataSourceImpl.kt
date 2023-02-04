package com.flow.moviesearch.data.datasource

import com.flow.moviesearch.data.dao.RecentQueryDao
import com.flow.moviesearch.presentation.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecentQueryDataSourceImpl @Inject constructor(
    private val dao: RecentQueryDao,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : RecentQueryDataSource {

    override suspend fun saveRecentQuery(query: String, timeMill: Long): Boolean {
        withContext(dispatcher) {
            dao.saveRecentQuery(query, timeMill)
        }
        return true
    }

    override suspend fun fetchRecentQueries() = withContext(dispatcher) { dao.fetchRecentQueries() }
}