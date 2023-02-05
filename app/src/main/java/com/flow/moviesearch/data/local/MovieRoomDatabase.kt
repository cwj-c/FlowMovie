package com.flow.moviesearch.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.flow.moviesearch.data.dao.RecentQueryDao

@Database(
    entities = [RecentQueryEntity::class],
    version = 1,
    exportSchema = true
)
abstract class MovieRoomDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: MovieRoomDatabase? = null

        fun getDatabase(context: Context): MovieRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieRoomDatabase::class.java,
                    "flow_movie_app_1.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

    abstract fun recentQueryDao(): RecentQueryDao
}