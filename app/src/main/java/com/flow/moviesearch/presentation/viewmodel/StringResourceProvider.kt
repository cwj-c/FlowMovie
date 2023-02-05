package com.flow.moviesearch.presentation.viewmodel

import androidx.annotation.StringRes
import com.flow.moviesearch.R

interface StringResourceProvider {
    fun getString(id: StringResourceId): String

    fun getString(id: StringResourceId, vararg formatArg: Any): String

    enum class StringResourceId(@StringRes val resId: Int) {
        Loading(R.string.loading),
        NetworkConnectFail(R.string.message_network_connection_fail),
        NoneSearchResult(R.string.message_none_search_result),
        NoneQueryHistory(R.string.message_none_query_history),
        NoneQuery(R.string.message_none_query),
        SearchFail(R.string.message_search_fail),
        LastPage(R.string.message_last_page)
    }
}