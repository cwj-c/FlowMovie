package com.flow.moviesearch.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = RecentQueryEntity.TABLE_NAME)
data class RecentQueryEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_QUERY)val query: String,
    @ColumnInfo(name = COLUMN_TIME)val time: Long
) {
    companion object {
        const val TABLE_NAME = "recent_query_table"
        const val COLUMN_QUERY = "query"
        const val COLUMN_TIME = "query_time"
    }
}