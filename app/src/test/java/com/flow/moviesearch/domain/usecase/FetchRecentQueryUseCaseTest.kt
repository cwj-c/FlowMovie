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
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@Suppress("NonAsciiCharacters")
internal class FetchRecentQueryUseCaseTest {

    private lateinit var useCase: FetchRecentQueryUseCase
    private lateinit var repository: MovieRepository
    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    @Before
    fun setUpTest() {
        repository = mockk(relaxed = true)
        useCase = FetchRecentQueryUseCase(
            repository,
            testDispatcher
        )
    }

    @Test
    fun `useCase는 Repository의 fetchRecentQuery가 에러를 전달하면 Result로 래핑해서 전달한다`() = runTest(testDispatcher) {
        val expect = Exception("unit test")
        coEvery { repository.fetchRecentQuery() } throws expect
        val actual = useCase()

        Assertions.assertThat(actual).isInstanceOf(Result::class.java)

        Assertions.assertThat(actual.exceptionOrNull())
            .isNotNull
            .isInstanceOf(java.lang.Exception::class.java)

        Assertions.assertThat(actual.exceptionOrNull()?.message)
            .isNotNull
            .isEqualTo(expect.message)
    }

    @Test
    fun `useCase는 Repository의 fetchRecentQuery의 결과를 Result로 래핑해서 전달한다`() = runTest(testDispatcher) {
        val expect = listOf("first", "second")
        coEvery { repository.fetchRecentQuery() } returns expect
        val actual = useCase()

        Assertions.assertThat(actual).isInstanceOf(Result::class.java)

        Assertions.assertThat(actual.getOrNull())
            .isNotNull
            .isEqualTo(expect)
    }

    @Test
    fun `useCase는 Repository의 fetchRecentQuery를 호출한다`() = runTest(testDispatcher)  {
        useCase()
        coVerify { repository.fetchRecentQuery() }
    }
}