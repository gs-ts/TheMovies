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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.themovies.domain.model.MovieDetails
import kotlinx.collections.immutable.ImmutableMap

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
        columns = GridCells.Fixed(3),
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
                    item(span = { GridItemSpan(3) }) { // Span across all columns
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
                    item(span = { GridItemSpan(3) }) {
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
    Card(modifier = modifier.fillMaxWidth()) {
        Column {
            AsyncImage(
                model = null,
                contentDescription = movieItem.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Adjust height as needed
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = movieItem.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Rating: ${movieItem.rating}",
                    fontSize = 14.sp
                )
                movieDetails?.let {
                    Text(text = "Budget: ${movieDetails.budget}", fontSize = 12.sp)
                    Text(text = "Revenue: ${movieDetails.revenue}", fontSize = 12.sp)
                }
            }
        }
    }
}
