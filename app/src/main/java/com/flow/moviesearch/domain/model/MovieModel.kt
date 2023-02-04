package com.flow.moviesearch.domain.model

data class MovieModel(
    val imageUrl: String,
    val title: String,
    val pubYear: Int,
    val rating: Double
)