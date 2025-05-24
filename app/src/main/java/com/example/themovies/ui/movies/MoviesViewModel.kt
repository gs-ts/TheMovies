package com.example.themovies.ui.movies

import androidx.lifecycle.ViewModel
import androidx.paging.map
import com.example.themovies.domain.usecase.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    val movies = getMoviesUseCase(page = 1)
        .map { pagingData ->
            pagingData.map { movie ->
                MovieItem(
                    id = movie.id,
                    title = movie.title
                )
            }
        }

    data class MovieItem(
        val id: Int,
        val title: String
    )
}
