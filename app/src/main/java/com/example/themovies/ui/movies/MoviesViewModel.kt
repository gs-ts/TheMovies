package com.example.themovies.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.themovies.domain.model.MovieDetails
import com.example.themovies.domain.usecase.GetMovieDetailsUseCase
import com.example.themovies.domain.usecase.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    getMoviesUseCase: GetMoviesUseCase,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModel() {

    private val _movieDetailsMap = MutableStateFlow<PersistentMap<Int, MovieDetails>>(persistentHashMapOf())
    val movieDetailsMap: StateFlow<PersistentMap<Int, MovieDetails>> = _movieDetailsMap.asStateFlow()

    // track already-requested IDs in order to avoid duplicate api calls
    private val requestedIds = mutableSetOf<Int>()

    val movies = getMoviesUseCase(page = 1)
        .map { pagingData ->
            pagingData.map { movie ->
                MovieItem(
                    id = movie.id,
                    title = movie.title,
                    rating = movie.rating
                )
            }
        }
        .cachedIn(viewModelScope)

    fun getMovieDetails(movieId: Int) {
        if (requestedIds.contains(movieId)) return
        requestedIds.add(movieId)

        viewModelScope.launch {
            getMovieDetailsUseCase(movieId).onSuccess { details ->
                _movieDetailsMap.update { current ->
                    current.put(movieId, details)
                }
            }
        }
    }

    data class MovieItem(
        val id: Int,
        val title: String,
        val rating: Float,
        val budget: Int? = null,
        val revenue: Int? = null
    )
}
