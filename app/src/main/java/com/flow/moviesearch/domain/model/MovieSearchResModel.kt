package com.flow.moviesearch.domain.model

data class MovieSearchResModel(
    val movies: List<MovieModel>,
    val curPage: Int
)