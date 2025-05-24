package com.example.themovies.data.dto

import com.example.themovies.domain.model.MovieDetails
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsDto(
    val id: Int,
    val budget: Long,
    val revenue: Long
) {

    fun toDomainModel(): MovieDetails {
        return MovieDetails(
            id = id,
            revenue = revenue,
            budget = budget
        )
    }
}
