@file:Suppress("MagicNumber")

package com.example.themovies.ui.mockdata

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.example.themovies.domain.model.MovieDetails
import com.example.themovies.ui.movies.MoviesViewModel.MovieItem
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.flow.flowOf

private val sampleMovieItemList = listOf(
    MovieItem(
        id = 1,
        title = "Movie 1",
        rating = 4.5f,
        posterUrl = "https://example.com/poster1.jpg"
    ),
    MovieItem(
        id = 2,
        title = "Movie 2",
        rating = 3.8f,
        posterUrl = "https://example.com/poster2.jpg"
    ),
    MovieItem(
        id = 3,
        title = "Movie 3",
        rating = 4.2f,
        posterUrl = "https://example.com/poster3.jpg"
    ),
    MovieItem(
        id = 4,
        title = "Movie 4",
        rating = 4.0f,
        posterUrl = "https://example.com/poster4.jpg"
    ),
    MovieItem(
        id = 5,
        title = "Movie 5 but with very long title",
        rating = 3.5f,
        posterUrl = "https://example.com/poster5.jpg"
    ),
    MovieItem(
        id = 6,
        title = "Movie 6",
        rating = 4.7f,
        posterUrl = "https://example.com/poster6.jpg"
    ),
    MovieItem(
        id = 7,
        title = "Movie 7",
        rating = 3.2f,
        posterUrl = "https://example.com/poster7.jpg"
    ),
    MovieItem(
        id = 8,
        title = "Movie 8",
        rating = 4.9f,
        posterUrl = "https://example.com/poster8.jpg"
    ),
    MovieItem(
        id = 9,
        title = "Movie 9",
        rating = 3.7f,
        posterUrl = "https://example.com/poster9.jpg"
    ),
    MovieItem(
        id = 10,
        title = "Movie 10",
        rating = 4.3f,
        posterUrl = "https://example.com/poster10.jpg"
    ),
    MovieItem(
        id = 11,
        title = "Movie 11",
        rating = 4.6f,
        posterUrl = "https://example.com/poster11.jpg"
    ),
    MovieItem(
        id = 12,
        title = "Movie 12",
        rating = 3.9f,
        posterUrl = "https://example.com/poster12.jpg"
    )
)

val sampleMovieDetailsMap = persistentMapOf(
    1 to MovieDetails(
        id = 1,
        budget = 100000000,
        revenue = 200000000
    ),
    2 to MovieDetails(
        id = 2,
        budget = 150000000,
        revenue = 250000000
    ),
    3 to MovieDetails(
        id = 3,
        budget = 80000000,
        revenue = 180000000
    ),
    4 to MovieDetails(
        id = 4,
        budget = 120000000,
        revenue = 220000000
    ),
    5 to MovieDetails(
        id = 5,
        budget = 90000000,
        revenue = 190000000
    ),
    6 to MovieDetails(
        id = 6,
        budget = 110000000,
        revenue = 210000000
    ),
    7 to MovieDetails(
        id = 7,
        budget = 70000000,
        revenue = 170000000
    ),
    8 to MovieDetails(
        id = 8,
        budget = 130000000,
        revenue = 230000000
    ),
)

val sampleMoviesPagingDataFlow = flowOf(
    PagingData.from(
        data = sampleMovieItemList,
        sourceLoadStates = LoadStates(
            refresh = LoadState.NotLoading(endOfPaginationReached = true),
            prepend = LoadState.NotLoading(endOfPaginationReached = true),
            append = LoadState.NotLoading(endOfPaginationReached = true)
        )
    )
)
