package com.flow.moviesearch.domain.model

data class MovieModel(
    val imageUrl: String,
    val title: String,
    val pubYear: String,
    val rating: Double,
    val linkUrl: String,
) {
    infix fun isSameItem(other: Any?): Boolean {
        return when (other) {
            is MovieModel -> this.title == other.title
            else -> false
        }
    }

    infix fun isSameContent(other: Any?): Boolean {
        return when (other) {
            is MovieModel -> this == other
            else -> false
        }
    }
}