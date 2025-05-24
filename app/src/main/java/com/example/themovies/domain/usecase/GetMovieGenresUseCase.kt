package com.example.themovies.domain.usecase

import com.example.themovies.domain.model.Genre
import com.example.themovies.domain.repository.MoviesRepository
import javax.inject.Inject

class GetMovieGenresUseCase @Inject constructor(private val repository: MoviesRepository) {

    suspend operator fun invoke(): Result<List<Genre>> = repository.getGenres()
}
