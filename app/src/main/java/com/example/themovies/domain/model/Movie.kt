package com.example.themovies.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val rating: Float,
    val posterUrl: String?
)

data class MovieDetails(
    val id: Int,
    val revenue: Long,
    val budget: Long,
)
