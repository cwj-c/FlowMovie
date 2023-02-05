package com.flow.moviesearch.data.datasource

import com.flow.moviesearch.data.local.RecentQueryEntity

interface RecentQueryDataSource {

    suspend fun saveRecentQuery(query: String, timeMill: Long): Boolean

    suspend fun fetchRecentQueries(): List<RecentQueryEntity>
}