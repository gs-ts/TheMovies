package com.example.themovies.domain.repository

import androidx.paging.PagingData
import com.example.themovies.domain.model.Genre
import com.example.themovies.domain.model.Movie
import com.example.themovies.domain.model.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getMovies(
        page: Int,
        genreId: Int? = null // if null then get all genres
    ): Flow<PagingData<Movie>>

    suspend fun getGenres(): Result<List<Genre>>

    suspend fun getMovieDetails(movieId: Int): Result<MovieDetails>
}
