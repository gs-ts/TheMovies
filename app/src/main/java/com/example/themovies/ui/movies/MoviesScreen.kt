package com.example.themovies.ui.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.example.themovies.domain.model.MovieDetails
import com.example.themovies.ui.mockdata.sampleMovieDetailsMap
import com.example.themovies.ui.mockdata.sampleMoviesPagingDataFlow
import com.example.themovies.ui.theme.TheMoviesTheme
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap

@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel,
    onNavigateToFilterScreen: () -> Unit
) {
    val movieItems = viewModel.movies.collectAsLazyPagingItems()
    val movieDetailsMap by viewModel.movieDetailsMap.collectAsState()

    // to remember the scroll state
    val lazyGridState = rememberSaveable(saver = LazyGridState.Saver) {
        LazyGridState()
    }

    MoviesContent(
        movieItems = movieItems,
        movieDetailsMap = movieDetailsMap,
        lazyGridState = lazyGridState,
        onGetMovieDetails = viewModel::getMovieDetails,
        onFilterClick = onNavigateToFilterScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MoviesContent(
    movieItems: LazyPagingItems<MoviesViewModel.MovieItem>,
    movieDetailsMap: ImmutableMap<Int, MovieDetails>,
    lazyGridState: LazyGridState,
    onGetMovieDetails: (id: Int) -> Unit,
    onFilterClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                ),
                title = {
                    Text("Movies")
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onFilterClick) {
                Icon(imageVector = Icons.Default.FilterList, contentDescription = "add")
            }
        }
    ) { innerPadding ->
        when (movieItems.loadState.refresh) {
            is LoadState.Loading -> { // initial loading state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is LoadState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "An error occurred while loading movies.")
                }
            }

            is LoadState.NotLoading -> {
                MoviesListView(
                    modifier = Modifier.padding(innerPadding),
                    movieItems = movieItems,
                    movieDetailsMap = movieDetailsMap,
                    lazyGridState = lazyGridState,
                    onGetMovieDetails = onGetMovieDetails
                )
            }
        }
    }
}

@Composable
private fun MoviesListView(
    lazyGridState: LazyGridState,
    movieItems: LazyPagingItems<MoviesViewModel.MovieItem>,
    movieDetailsMap: ImmutableMap<Int, MovieDetails>,
    onGetMovieDetails: (id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(GRID_CELL_SIZE),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        state = lazyGridState,
        modifier = modifier
    ) {
        items(movieItems.itemCount) { index ->
            movieItems[index]?.let { movieItem ->
                val movieDetails = movieDetailsMap[movieItem.id]
                if (movieDetails == null) { // fetch the details
                    onGetMovieDetails(movieItem.id)
                }

                MovieCard(
                    movieItem = movieItem,
                    movieDetails = movieDetails
                )
            }
        }

        // handle state when loading more items
        movieItems.apply {
            when (loadState.append) {
                is LoadState.Loading -> {
                    item(span = { GridItemSpan(GRID_CELL_SIZE) }) { // Span across all columns
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                is LoadState.Error -> {
                    item(span = { GridItemSpan(GRID_CELL_SIZE) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Error loading more movies.")
                        }
                    }
                }

                is LoadState.NotLoading -> {}
            }
        }
    }
}

@Composable
private fun MovieCard(
    movieItem: MoviesViewModel.MovieItem,
    movieDetails: MovieDetails?,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Column {
            SubcomposeAsyncImage(
                model = movieItem.posterUrl,
                contentDescription = movieItem.title,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = movieItem.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Rating: ${movieItem.rating}",
                    fontSize = 12.sp
                )
                movieDetails?.let {
                    Text(
                        text = "Budget: ${movieDetails.budget}",
                        fontSize = 10.sp
                    )
                    Text(
                        text = "Revenue: ${movieDetails.revenue}",
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

private const val GRID_CELL_SIZE = 3

@Preview
@Composable
private fun MoviesPreview() {
    TheMoviesTheme {
        val movieLazyPagingItems = sampleMoviesPagingDataFlow.collectAsLazyPagingItems()
        MoviesContent(
            movieItems = movieLazyPagingItems,
            movieDetailsMap = sampleMovieDetailsMap.toImmutableMap(),
            lazyGridState = LazyGridState(),
            onGetMovieDetails = {},
            onFilterClick = {}
        )
    }
}
