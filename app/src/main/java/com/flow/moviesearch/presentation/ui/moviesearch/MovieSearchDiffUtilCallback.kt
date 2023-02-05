package com.flow.moviesearch.presentation.ui.moviesearch

import androidx.recyclerview.widget.DiffUtil
import com.flow.moviesearch.domain.model.MovieModel

class MovieSearchDiffUtilCallback(
    private val oldList: List<MovieModel>,
    private val newList: List<MovieModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] isSameItem newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] isSameContent  newList[newItemPosition]
    }
}