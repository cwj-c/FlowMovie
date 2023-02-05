package com.flow.moviesearch.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flow.moviesearch.domain.usecase.FetchRecentQueryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentQueryHistoryViewModel @Inject constructor(
    private val fetchRecentQueryUseCase: FetchRecentQueryUseCase,
    private val stringResProvider: StringResourceProvider
) : ViewModel() {

    private val _dataLoading = MutableStateFlow(false)
    val dataLoading = _dataLoading.asStateFlow()

    private val _history = MutableStateFlow<List<String>>(emptyList())
    val history = _history.asStateFlow()

    private val _uiState = MutableSharedFlow<UiState>()
    val uiState = _uiState.asSharedFlow()

    init {
        viewModelScope.launch {
            _dataLoading.value = true
            fetchRecentQueryUseCase()
                .onSuccess {
                    if(it.isEmpty()){
                        finishView(stringResProvider.getString(StringResourceProvider.StringResourceId.NoneQueryHistory))
                    }
                    _history.value = it
                }.onFailure {
                    finishView(stringResProvider.getString(StringResourceProvider.StringResourceId.SearchFail) + "\n${it.message}")
                }.also {
                    _dataLoading.value = false
                }
        }
    }

    fun historyClickEvent(query: String) = viewModelScope.launch {
        _uiState.emit(UiState.HistoryClick(query))
    }

    private fun finishView(message: String?) = viewModelScope.launch {
        _uiState.emit(UiState.FinishView(message))
    }

    sealed class UiState {
        data class FinishView(val message: String?) : UiState()
        data class HistoryClick(val query: String) : UiState()
    }

}