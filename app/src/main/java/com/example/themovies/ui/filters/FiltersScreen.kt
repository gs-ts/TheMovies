package com.example.themovies.ui.filters

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.themovies.domain.model.Genre

@Composable
fun FiltersScreen(viewModel: FiltersViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    FiltersContent(
        state = state,
        onFilterSelect = viewModel::onFilterSelect
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FiltersContent(
    state: FiltersViewModel.State,
    onFilterSelect: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                ),
                title = {
                    Text("Filters")
                }
            )
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (state.hasError) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "An error occurred")
            }
        }

        if (state.genres.isNotEmpty()) {
            Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                state.genres.forEach { genre ->
                    FilterItem(
                        genre = genre,
                        onFilterSelect = {
                            onFilterSelect(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterItem(
    genre: Genre,
    onFilterSelect: (Int) -> Unit
) {
    Text(text = genre.name)
}
