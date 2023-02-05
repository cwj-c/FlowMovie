package com.flow.moviesearch.presentation.ui.moviesearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.flow.moviesearch.databinding.ItemMovieSearchResultBinding
import com.flow.moviesearch.domain.model.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieSearchAdapter(
    private val itemClickListener: (MovieModel) -> Unit
) : RecyclerView.Adapter<MovieSearchAdapter.MovieSearchResultViewHolder>() {

    private var movies: List<MovieModel> = emptyList()
    private fun setList(newList: List<MovieModel>) {
        movies = newList
    }

    suspend fun updateList(newList: List<MovieModel>) {
        withContext(Dispatchers.Default) {
            val diffRes = DiffUtil.calculateDiff(MovieSearchDiffUtilCallback(movies, newList))
            withContext(Dispatchers.Main) {
                setList(newList)
                diffRes.dispatchUpdatesTo(this@MovieSearchAdapter)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieSearchResultViewHolder {
        return MovieSearchResultViewHolder.from(parent, itemClickListener)
    }

    override fun onBindViewHolder(holder: MovieSearchResultViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    class MovieSearchResultViewHolder(
        private val binding: ItemMovieSearchResultBinding,
        val itemClickListener: (MovieModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(
                parent: ViewGroup,
                itemClickListener: (MovieModel) -> Unit
            ): MovieSearchResultViewHolder {
                return MovieSearchResultViewHolder(
                    ItemMovieSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                    itemClickListener
                )
            }
        }

        fun bind(movie: MovieModel) {
            binding.holder = this
            binding.movie = movie
        }
    }
}