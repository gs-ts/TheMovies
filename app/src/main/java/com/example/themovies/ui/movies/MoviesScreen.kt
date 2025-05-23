package com.example.themovies.ui.movies

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MoviesScreen(viewModel: MoviesViewModel) {
    MoviesContent()
}

@Composable
private fun MoviesContent() {
    Text(text = "movies")
}
