package com.example.themovies.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.themovies.ui.filters.FiltersScreen
import com.example.themovies.ui.movies.MoviesScreen
import kotlinx.serialization.Serializable

@Serializable
object Movies

@Serializable
object Filters

@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Movies
    ) {
        composable<Movies> {
            MoviesScreen(
                viewModel = hiltViewModel()
            )
        }
        composable<Filters> {
            FiltersScreen(
                viewModel = hiltViewModel()
            )
        }
    }
}
