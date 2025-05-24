package com.example.themovies.domain.usecase

import com.example.themovies.domain.model.MovieDetails
import com.example.themovies.domain.repository.MoviesRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(private val repository: MoviesRepository) {

    suspend operator fun invoke(movieId: Int): Result<MovieDetails> = repository.getMovieDetails(movieId = movieId)
}
