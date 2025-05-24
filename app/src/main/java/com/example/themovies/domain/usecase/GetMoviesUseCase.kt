package com.example.themovies.domain.usecase

import androidx.paging.PagingData
import com.example.themovies.domain.model.Movie
import com.example.themovies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val repository: MoviesRepository) {

    operator fun invoke(
        page: Int,
        genreId: Int? = null
    ): Flow<PagingData<Movie>> = repository.getMovies(page, genreId)
}
