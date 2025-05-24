package com.example.themovies.data.network

import com.example.themovies.data.dto.GenreListResponse
import com.example.themovies.data.dto.MovieDetailsDto
import com.example.themovies.data.dto.MovieListResponse

interface TmdbApi {

    suspend fun getMovies(
        page: Int,
        genreId: Int? = null // if null then get all genres
    ): MovieListResponse

    suspend fun getGenres(): Result<GenreListResponse>

    suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsDto>
}
