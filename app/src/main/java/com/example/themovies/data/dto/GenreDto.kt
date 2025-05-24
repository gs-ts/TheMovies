package com.example.themovies.data.dto

import com.example.themovies.domain.model.Genre
import kotlinx.serialization.Serializable

@Serializable
data class GenreListResponse(
    val genres: List<GenreDto>
)

@Serializable
data class GenreDto(
    val id: Int,
    val name: String
) {

    fun toDomainModel(): Genre {
        return Genre(
            id = id,
            name = name
        )
    }
}
