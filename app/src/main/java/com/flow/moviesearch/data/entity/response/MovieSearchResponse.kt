package com.flow.moviesearch.data.entity.response


import com.flow.moviesearch.domain.model.MovieModel
import com.flow.moviesearch.domain.model.MovieSearchResModel
import com.google.gson.annotations.SerializedName

data class MovieSearchResponse(
    @SerializedName("display")
    val display: Int,
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("lastBuildDate")
    val lastBuildDate: String,
    @SerializedName("start")
    val start: Int,
    @SerializedName("total")
    val total: Int
) {
    fun toModel(curPage: Int): MovieSearchResModel = MovieSearchResModel(
        movies = items.map { it.toModel() },
        curPage = curPage
    )

    data class Item(
        @SerializedName("actor")
        val actor: String,
        @SerializedName("director")
        val director: String,
        @SerializedName("image")
        val image: String,
        @SerializedName("link")
        val link: String,
        @SerializedName("pubDate")
        val pubDate: String,
        @SerializedName("subtitle")
        val subtitle: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("userRating")
        val userRating: Double
    ) {
        fun toModel(): MovieModel = MovieModel(
            imageUrl = image,
            title = title,
            pubYear = pubDate,
            rating = userRating
        )
    }
}