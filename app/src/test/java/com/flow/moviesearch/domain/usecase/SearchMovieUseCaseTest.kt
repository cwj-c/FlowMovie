package com.flow.moviesearch.domain.usecase

import com.flow.moviesearch.domain.model.MovieSearchResModel
import com.flow.moviesearch.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@Suppress("NonAsciiCharacters")
internal class SearchMovieUseCaseTest {

    private lateinit var useCase: SearchMovieUseCase
    private lateinit var repository: MovieRepository
    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    @Before
    fun setUpTest() {
        repository = mockk(relaxed = true)
        useCase = SearchMovieUseCase(
            repository,
            testDispatcher
        )
    }

    @Test
    fun `useCase는 Repository의 saveRecentQuery의 결과는 무시한다`() = runTest(testDispatcher) {
        val expect: MovieSearchResModel = mockk(relaxed = true)
        coEvery { repository.searchMovie(any(), any()) } returns expect
        coEvery { repository.saveRecentQuery(any()) } throws Exception("unit test")
        val actual = useCase("query", 0)

        assertThat(actual).isInstanceOf(Result::class.java)

        assertThat(actual.exceptionOrNull())
            .isNull()

        assertThat(actual.getOrNull())
            .isNotNull
            .isEqualTo(expect)
    }

    @Test
    fun `useCase는 Repository의 searchMovie가 에러를 전달하면 Result로 래핑해서 전달한다`() = runTest(testDispatcher) {
        val expect = Exception("unit test")
        coEvery { repository.searchMovie(any(), any()) } throws expect
        val actual = useCase("query", 0)

        assertThat(actual).isInstanceOf(Result::class.java)

        assertThat(actual.exceptionOrNull())
            .isNotNull
            .isInstanceOf(java.lang.Exception::class.java)

        assertThat(actual.exceptionOrNull()?.message)
            .isNotNull
            .isEqualTo(expect.message)
    }

    @Test
    fun `useCase는 Repository의 searchMovie의 결과를 Result로 래핑해서 전달한다`() = runTest(testDispatcher) {
        val expect: MovieSearchResModel = mockk(relaxed = true)
        coEvery { repository.searchMovie(any(), any()) } returns expect
        val actual = useCase("query", 0)

        assertThat(actual).isInstanceOf(Result::class.java)

        assertThat(actual.getOrNull())
            .isNotNull
            .isEqualTo(expect)
    }

    @Test
    fun `useCase는 Repository의 searchMovie가 에러를 전달하지 않으면 매개변수 query로 saveRecentQuery를 호출한다`() = runTest(testDispatcher)  {
        val expect = "query"
        coEvery { repository.searchMovie(any(), any()) } returns mockk()
        useCase(expect, 0)
        coVerify { repository.saveRecentQuery(expect) }
    }

    @Test
    fun `useCase는 Repository의 searchMovie가 에러를 전달하지 않으면 saveRecentQuery를 이어서 호출한다`() = runTest(testDispatcher)  {
        coEvery { repository.searchMovie(any(), any()) } returns mockk()
        useCase("query", 0)
        coVerify { repository.searchMovie(any(), any()) }
        coVerify { repository.saveRecentQuery(any()) }
    }

    @Test
    fun `useCase는 Repository의 searchMovie를 호출한다`() = runTest(testDispatcher)  {
        useCase("query", 0)
        coVerify { repository.searchMovie(any(), any()) }
    }

}