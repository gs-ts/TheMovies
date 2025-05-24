package com.example.themovies.ui.movies

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.themovies.domain.model.MovieDetails
import com.example.themovies.domain.usecase.GetMovieDetailsUseCase
import com.example.themovies.domain.usecase.GetMoviesUseCase
import com.example.themovies.ui.Movies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getMoviesUseCase: GetMoviesUseCase,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModel() {

    private val genreId: Int? = savedStateHandle.toRoute<Movies>().genreId?.toInt()

    private val movies = getMoviesUseCase(genreId = genreId).cachedIn(viewModelScope)

    private val _movieDetailsMap = MutableStateFlow<PersistentMap<Int, MovieDetails>>(persistentHashMapOf())
    val movieDetailsMap: StateFlow<PersistentMap<Int, MovieDetails>> = _movieDetailsMap.asStateFlow()

    // track already-requested IDs in order to avoid duplicate api calls
    private val requestedIds = mutableSetOf<Int>()

    val movieItems: Flow<PagingData<MovieItem>> = combine(
        movies,
        movieDetailsMap
    ) { pagingData, detailsMap ->
        pagingData.map { movie ->
            val movieDetails = detailsMap[movie.id]
            MovieItem(
                id = movie.id,
                title = movie.title,
                rating = movie.rating,
                posterUrl = movie.posterUrl,
                budget = movieDetails?.budget,
                revenue = movieDetails?.revenue
            )
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        PagingData.empty()
    )

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
        val posterUrl: String?,
        val budget: Long? = null,
        val revenue: Long? = null
    )
}
