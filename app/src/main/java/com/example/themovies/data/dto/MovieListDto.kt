package com.example.themovies.data.dto

import com.example.themovies.domain.model.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieListResponse(
    val page: Int? = null,
    val results: List<MovieListItemDto>? = null,
    @SerialName("total_pages")
    val totalPages: Int? = null,
    @SerialName("total_results")
    val totalResults: Int? = null
)

@Serializable
data class MovieListItemDto(
    val id: Int,
    @SerialName("poster_path")
    val posterPath: String?,
    val title: String,
    @SerialName("vote_average")
    val rating: Double
) {

    fun toDomainModel() = Movie(
        id = id,
        title = title,
        rating = rating.toFloat(),
        posterUrl = posterPath?.let { "$posterBaseUrl$it" }
    )

    companion object {
        private const val posterBaseUrl = "https://image.tmdb.org/t/p/w185"
    }
}
