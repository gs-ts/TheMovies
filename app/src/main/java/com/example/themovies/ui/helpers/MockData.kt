@file:Suppress("MagicNumber")

package com.example.themovies.ui.helpers

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.example.themovies.ui.filters.FiltersViewModel.State.Filter
import com.example.themovies.ui.movies.MoviesViewModel.MovieItem
import kotlinx.coroutines.flow.flowOf

private val sampleMovieItemList = listOf(
    MovieItem(
        id = 1,
        title = "Movie 1",
        rating = 4.5f,
        posterUrl = "https://example.com/poster1.jpg",
        budget = 100000000,
        revenue = 200000000
    ),
    MovieItem(
        id = 2,
        title = "Movie 2",
        rating = 3.8f,
        posterUrl = "https://example.com/poster2.jpg",
        budget = 150000000,
        revenue = 250000000
    ),
    MovieItem(
        id = 3,
        title = "Movie 3",
        rating = 4.2f,
        posterUrl = "https://example.com/poster3.jpg",
        budget = 80000000,
        revenue = 180000000
    ),
    MovieItem(
        id = 4,
        title = "Movie 4",
        rating = 4.0f,
        posterUrl = "https://example.com/poster4.jpg",
        budget = 120000000,
        revenue = 220000000
    ),
    MovieItem(
        id = 5,
        title = "Movie 5 but with very long title",
        rating = 3.5f,
        posterUrl = "https://example.com/poster5.jpg",
        budget = 90000000,
        revenue = 190000000
    ),
    MovieItem(
        id = 6,
        title = "Movie 6",
        rating = 4.7f,
        posterUrl = "https://example.com/poster6.jpg",
        budget = 110000000,
        revenue = 210000000
    ),
    MovieItem(
        id = 7,
        title = "Movie 7",
        rating = 3.2f,
        posterUrl = "https://example.com/poster7.jpg",
        budget = 70000000,
        revenue = 170000000
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

val filterItems = listOf(
    Filter(1, "Action", isSelected = false),
    Filter(2, "Comedy", isSelected = false),
    Filter(3, "Drama", isSelected = false),
    Filter(4, "Thriller", isSelected = false),
    Filter(5, "Horror", isSelected = false),
    Filter(6, "Romance", isSelected = false),
    Filter(7, "Sci-Fi", isSelected = false),
    Filter(8, "Mystery", isSelected = true),
    Filter(9, "Animation", isSelected = false),
    Filter(10, "Adventure", isSelected = false),
    Filter(11, "Fantasy", isSelected = false),
    Filter(12, "Crime", isSelected = false),
    Filter(13, "Family", isSelected = false),
    Filter(14, "History", isSelected = false),
    Filter(15, "Music", isSelected = false)
)
