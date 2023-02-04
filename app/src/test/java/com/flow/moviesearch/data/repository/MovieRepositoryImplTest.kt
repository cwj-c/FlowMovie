package com.flow.moviesearch.data.repository

import com.flow.moviesearch.data.constant.NetworkConstant
import com.flow.moviesearch.data.datasource.MovieSearchDataSource
import com.flow.moviesearch.data.datasource.RecentQueryDataSource
import com.flow.moviesearch.data.entity.response.MovieSearchResponse
import com.flow.moviesearch.data.local.RecentQueryEntity
import com.flow.moviesearch.domain.model.DomainException
import com.flow.moviesearch.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
@Suppress("NonAsciiCharacters")
internal class MovieRepositoryImplTest {

    private lateinit var repository: MovieRepository
    private lateinit var searchDataSource: MovieSearchDataSource
    private lateinit var recentQueryDataSource: RecentQueryDataSource
    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    private val dummy = MovieSearchResponse(
        total = 20,
        items = listOf(
            MovieSearchResponse.Item(
                image = "image",
                pubDate = "2022",
                title = "title",
                userRating = 0.1,
                actor = "a", director = "d", link = "l", subtitle = "s"
            )), display = 0, lastBuildDate = "l", start = 0
    )

    @Before
    fun setUpTest() {
        searchDataSource = mockk(relaxed = true)
        recentQueryDataSource = mockk(relaxed = true)
        repository = MovieRepositoryImpl(
            searchDataSource,
            recentQueryDataSource,
            testDispatcher
        )
    }

    @Test
    fun `개인 page 관리 테스트 케이스`() = runTest(testDispatcher) {
        var page = 1
        val total = 21
        coEvery { searchDataSource.searchMovie(any(), any()) } returns dummy.copy(total = total)
        repeat(total/NetworkConstant.RESPONSE_DISPLAY) {
            repository.searchMovie("query", page++)
        }
        repository.searchMovie("query", page)
        val start = (page-1) * NetworkConstant.RESPONSE_DISPLAY + 1 // 이전 페이지 마지막 Item + 1

        coVerify { searchDataSource.searchMovie(any(), start) }
    }

    @Test
    fun `repository는 이전 MovieSearchDataSource의 searchMovie결과의 total을 기억해두고, 다음 요청시에 start를 계산하여 searchMovie를 호출한다`() = runTest(testDispatcher) {
        var page = 1
        coEvery { searchDataSource.searchMovie(any(), any()) } returns dummy.copy(total = 40)
        repository.searchMovie("query", page++) // total 을 21로 저장
        repository.searchMovie("query", page) // 페이지 2로 요청, start 계산 수행
        val start = (page-1) * NetworkConstant.RESPONSE_DISPLAY + 1 // 이전 페이지 마지막 Item + 1

        coVerify { searchDataSource.searchMovie(any(), start) }
    }

    @Test
    fun `repository는 이전 MovieSearchDataSource의 searchMovie결과를 기억해두고, 다음 요청시에 페이지 계산을 진행하여 다음 페이지가 없다면 MaxPageException을 발생시킨다`() = runTest(testDispatcher) {
        val page = 1
        coEvery { searchDataSource.searchMovie(any(), page) } returns mockk<MovieSearchResponse>(relaxed = true).copy(total = 20)
        repository.searchMovie("query", page)

        val actual = catchThrowable {
            runTest(testDispatcher) {
                repository.searchMovie("query", page + 1)
            }
        }

        assertThat(actual)
            .isInstanceOf(DomainException.MaxPageException::class.java)
    }

    @Test
    fun `repository는 다음 페이지가 없다면 MaxPageException을 발생시킨다`() {
        val actual = catchThrowable {
            runTest(testDispatcher) {
                repository.searchMovie("query", 2)
            }
        }

        assertThat(actual)
            .isInstanceOf(DomainException.MaxPageException::class.java)
    }

    @Test
    fun `repository는 searchMovie의 매개변수 page를 결과인 MovieSearchResModel의 curPage로 적용해준다`() = runTest(testDispatcher) {
        val expect = dummy.copy()
        coEvery { searchDataSource.searchMovie(any(), any()) } returns expect
        val page = 1
        val actual = repository.searchMovie("query", page)

        assertThat(actual.curPage)
            .isEqualTo(expect.toModel(page).curPage)
    }

    @Test
    fun `repository는 MovieSearchDataSource의 searchMovie의 리턴값을 MovieSearchResModel로 가공해 리턴한다`() = runTest(testDispatcher) {
        val expect = dummy.copy()
        coEvery { searchDataSource.searchMovie(any(), any()) } returns expect
        val page = 1
        val actual = repository.searchMovie("query", page)

        assertThat(actual)
            .isEqualTo(expect.toModel(page))
    }

    @Test
    fun `searchMovie는 매개변수 page로 0이나 1이들어오면 MovieSearchDataSource의 searchMovie의 매개변수 start를 1로 호출한다`() = runTest(testDispatcher) {
        repository.searchMovie("query", 0)
        coVerify { searchDataSource.searchMovie("query", 1) }
    }

    @Test
    fun `searchMovie는 MovieSearchDataSource의 searchMovie를 호출한다`() = runTest(testDispatcher) {
        repository.searchMovie("query", 1)
        coVerify { searchDataSource.searchMovie(any(), any()) }
    }

    @Test
    fun `saveRecentQuery는 RecentQueryDataSource의 saveRecentQuery를 호출한다`() = runTest(testDispatcher) {
        repository.saveRecentQuery("query")
        coVerify { recentQueryDataSource.saveRecentQuery(any(), any()) }
    }

    @Test
    fun `fetchRecentQuery RecentQueryDataSource의 fetchRecentQueries결과를 String리스트로 가공하여 리턴한다`() = runTest(testDispatcher) {
        val expect = Array(5){ RecentQueryEntity((100+it).toString(), (100+it).toLong()) }.toList()
        coEvery { recentQueryDataSource.fetchRecentQueries() } returns expect

        val actual = repository.fetchRecentQuery()

        assertThat(actual)
            .hasSize(expect.size)
            .containsAll(expect.map{it.query})
    }

    @Test
    fun `fetchRecentQuery RecentQueryDataSource의 fetchRecentQueries 호출한다`() = runTest(testDispatcher) {
        repository.fetchRecentQuery()
        coVerify { recentQueryDataSource.fetchRecentQueries() }
    }
}