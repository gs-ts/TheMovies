package com.example.themovies.data.network

import com.example.themovies.data.dto.MovieListResponse
import com.example.themovies.domain.model.Genre
import com.example.themovies.domain.model.MovieDetails

interface TmdbApi {

    suspend fun getMovies(
        page: Int,
        genreId: Int? = null // if null then get all genres
    ): MovieListResponse

    suspend fun getGenres(): Result<List<Genre>>

    suspend fun getMovieDetails(movieId: Int): Result<MovieDetails>
}
