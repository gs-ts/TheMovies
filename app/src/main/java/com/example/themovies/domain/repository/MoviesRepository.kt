package com.example.themovies.domain.repository

import com.example.themovies.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getMovies(): Flow<Movie>
}
