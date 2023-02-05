package com.flow.moviesearch.presentation.viewmodel

import android.content.Context

class StringResourceProviderImpl(
    private val context: Context
) : StringResourceProvider {

    override fun getString(id: StringResourceProvider.StringResourceId): String {
        return context.resources.getString(id.resId)
    }

    override fun getString(
        id: StringResourceProvider.StringResourceId,
        vararg formatArg: Any
    ): String {
        return context.resources.getString(id.resId, *formatArg)
    }
}