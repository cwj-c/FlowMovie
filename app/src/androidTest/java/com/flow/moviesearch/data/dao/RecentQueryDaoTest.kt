package com.flow.moviesearch.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.flow.moviesearch.data.local.MovieRoomDatabase
import com.flow.moviesearch.data.local.RecentQueryEntity
import io.mockk.InternalPlatformDsl.toStr
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@Suppress("NonAsciiCharacters")
internal class RecentQueryDaoTest {

    private lateinit var dao: RecentQueryDao
    private lateinit var db: MovieRoomDatabase
    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    private val recentQuery = RecentQueryEntity("query1", 1001)

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, MovieRoomDatabase::class.java
        ).build()

        dao = db.recentQueryDao()
    }

    @Test
    fun fetchRecentQueries_returns_list_with_sort_by_time() = runTest(testDispatcher) {
        dao.insertRecentQuery(recentQuery)
        val actual = dao.fetchRecentQueries()

        assertThat(actual)
            .contains(recentQuery)
            .hasSize(1)
            .isSortedAccordingTo(compareBy { -it.time })
    }

    @Test
    fun insertRecentQuery_conflict_replace() = runTest(testDispatcher) {
        val expect = recentQuery.copy(time = 1001)
        dao.insertRecentQuery(expect.copy(time = 2002))
        dao.insertRecentQuery(expect)

        val actual = dao.fetchRecentQueries()

        assertThat(actual)
            .hasSize(1)
            .contains(expect)
    }

    @Test
    fun removeRecentQueries_remove_without_last_20_item() = runTest(testDispatcher) {
        val entities = Array(25) { RecentQueryEntity(query = (100+it).toString(), time = (100+it).toLong()) }
        entities.forEach {
            dao.insertRecentQuery(it)
        }
        dao.removeRecentQueries()

        entities.sortBy { -it.time }
        val actual = dao.fetchRecentQueries()

        assertThat(actual)
            .hasSize(20)
            .contains(entities[0])
            .contains(entities[19])

    }

    @Test
    fun saveRecentQuery_save_maximum_last_20_item() = runTest(testDispatcher) {
        val entities = Array(25) { RecentQueryEntity(query = (100+it).toString(), time = (100+it).toLong()) }
        entities.forEach {
            dao.saveRecentQuery(it.query, it.time)
        }
        entities.sortBy { -it.time }
        val actual = dao.fetchRecentQueries()

        assertThat(actual)
            .hasSize(20)
            .contains(entities[0])
            .contains(entities[19])
    }

}