package com.flow.moviesearch.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flow.moviesearch.domain.model.DomainException
import com.flow.moviesearch.domain.model.MovieModel
import com.flow.moviesearch.domain.model.MovieSearchResModel
import com.flow.moviesearch.domain.usecase.SearchMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieSearchViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val searchMovieUseCase: SearchMovieUseCase,
    private val stringResProvider: StringResourceProvider
) : ViewModel() {
    companion object {
        private const val KEY_SEARCH_INFO = "key_search_info"
    }

    private val _dataLoading = MutableStateFlow(false)
    val dataLoading = _dataLoading.asStateFlow()

    private val _uiState = MutableSharedFlow<UiState>()
    val uiState = _uiState.asSharedFlow()

    private val uiAction = MutableSharedFlow<UiAction>().also { flow ->
        flow.filterIsInstance<UiAction.Input>()
            .debounce(500L)
            .filter { it.input.isNotBlank() && recentQuery.value != it.input }
            .onEach {
                handle[KEY_SEARCH_INFO] = SearchInfo(it.input, 1)
            }.launchIn(viewModelScope)
    }

    private val _searchInfo: StateFlow<SearchInfo> = handle.getStateFlow(KEY_SEARCH_INFO, SearchInfo("", 1))
        .also { flow ->
            flow.onEach {
                uiAction.emit(UiAction.Search(it.query, it.page))
            }.launchIn(viewModelScope)
        }
    val recentQuery: StateFlow<String> = _searchInfo
        .map { it.query }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ""
        )

    private val movieSearchResult = MutableStateFlow(MovieSearchResModel(emptyList(), 1))
    val movieSearchList: StateFlow<List<MovieModel>> = movieSearchResult
        .map { it.movies }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    init {
        uiAction.filterIsInstance<UiAction.Search>().also { searchFlow ->
            searchFlow.onEach {
                _uiState.emit(UiState.KeyboardShown(false))
            }.launchIn(viewModelScope)

            searchFlow.filter { it.page == 1 || it.page == 0 }
                .onEach {
                    when (dataLoading.value) {
                        true -> showToast(stringResProvider.getString(StringResourceProvider.StringResourceId.Loading))
                        else -> {
                            _dataLoading.value = true
                            processSearchResult(searchMovieUseCase(it.query, 1))
                        }
                    }
                }.launchIn(viewModelScope)

            searchFlow.filter { it.page > 1 }
                .filter { !dataLoading.value }
                .onEach {
                    _dataLoading.value = true
                    processSearchResult(searchMovieUseCase(it.query, it.page))
                }.launchIn(viewModelScope)
        }
    }

    private fun processSearchResult(result: Result<MovieSearchResModel>) {
        result.onSuccess {
            when (it.curPage) {
                0, 1 -> movieSearchResult.value = it
                else -> movieSearchResult.value = movieSearchResult.value.copy(
                    curPage = it.curPage,
                    movies = movieSearchList.value + it.movies
                )
            }
            if(it.movies.isEmpty()) {
                showToast(stringResProvider.getString(StringResourceProvider.StringResourceId.NoneSearchResult))
            }
        }.onFailure {
            when (it) {
                is DomainException.MaxPageException ->
                    showToast(stringResProvider.getString(StringResourceProvider.StringResourceId.LastPage))
                is DomainException.NetworkConnectionException ->
                    showToast(stringResProvider.getString(StringResourceProvider.StringResourceId.NetworkConnectFail))
                else -> {
                    showToast(stringResProvider.getString(StringResourceProvider.StringResourceId.SearchFail) + "\n${it.message}")
                }
            }
        }.also {
            _dataLoading.value = false
        }
    }


    private fun doSearch(query: String) {
        if(query.isBlank()) {
            showToast(stringResProvider.getString(StringResourceProvider.StringResourceId.NoneQuery))
            return
        }
        viewModelScope.launch {
            when (query) {
                recentQuery.value -> uiAction.emit(UiAction.Search(query, 1))
                else -> handle[KEY_SEARCH_INFO] = SearchInfo(query, 1)
            }
        }
    }

    fun inputFinishEvent(input: String) = doSearch(input)

    fun inputChangedEvent(input: CharSequence) = viewModelScope.launch {
        uiAction.emit(UiAction.Input(input.toString()))
    }

    fun nextPageEvent() {
        handle[KEY_SEARCH_INFO] = SearchInfo(recentQuery.value, movieSearchResult.value.curPage + 1)
    }

    fun movieClickEvent(movie: MovieModel) = viewModelScope.launch {
        _uiState.emit(UiState.KeyboardShown(false))
        _uiState.emit(UiState.NavigateMovieUrl(movie.linkUrl))
    }

    fun recentQueryHistoryEvent() = viewModelScope.launch {
        _uiState.emit(UiState.NavigateRecentQueryHistory)
    }

    fun backgroundTouchEvent() {
        viewModelScope.launch { _uiState.emit(UiState.KeyboardShown(false)) }
    }

    private fun showToast(message: String) = viewModelScope.launch { _uiState.emit(UiState.ShowToast(message)) }

    private class SearchInfo(
        val query: String,
        val page: Int
    ) : java.io.Serializable

    sealed class UiAction {
        data class Input(val input: String) : UiAction()
        data class Search(val query: String, val page: Int) : UiAction()
    }

    sealed class UiState {
        data class ShowToast(val message: String) : UiState()
        data class KeyboardShown(val visible: Boolean) : UiState()
        object NavigateRecentQueryHistory : UiState()
        data class NavigateMovieUrl(val url: String) : UiState()
    }
}