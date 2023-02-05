package com.flow.moviesearch.data.dao

import androidx.room.*
import com.flow.moviesearch.data.constant.MovieHistoryConstant
import com.flow.moviesearch.data.local.RecentQueryEntity

@Dao
interface RecentQueryDao {

    @Query("SELECT * FROM ${RecentQueryEntity.TABLE_NAME} ORDER BY ${RecentQueryEntity.COLUMN_TIME} DESC")
    suspend fun fetchRecentQueries(): List<RecentQueryEntity>

    @Transaction
    suspend fun saveRecentQuery(query: String, timeMill: Long) {
        insertRecentQuery(RecentQueryEntity(query, timeMill))
        removeRecentQueries()
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentQuery(query: RecentQueryEntity)

    @Query("DELETE FROM ${RecentQueryEntity.TABLE_NAME} where ${RecentQueryEntity.COLUMN_TIME} NOT IN " +
        "(SELECT ${RecentQueryEntity.COLUMN_TIME} from ${RecentQueryEntity.TABLE_NAME} ORDER BY ${RecentQueryEntity.COLUMN_TIME} DESC LIMIT ${MovieHistoryConstant.HISTORY_MAX_SIZE})")
    suspend fun removeRecentQueries()
}