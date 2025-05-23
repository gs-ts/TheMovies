package com.example.themovies.ui.filters

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FiltersScreen(viewModel: FiltersViewModel) {
    FiltersContent()
}

@Composable
private fun FiltersContent() {
    Text(text = "Filters")
}
