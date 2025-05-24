@file:Suppress("MagicNumber")

package com.example.themovies.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.themovies.ui.filters.FiltersScreen
import com.example.themovies.ui.filters.FiltersViewModel.Companion.ALL_GENRE_ID
import com.example.themovies.ui.movies.MoviesScreen
import kotlinx.serialization.Serializable

@Serializable
data class Movies(val genreId: Int? = null)

@Serializable
data class Filters(val selectedGenreId: Int? = null)

@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Movies()
    ) {
        composable<Movies>(
            enterTransition = enterRight,
            exitTransition = exitLeft,
            popEnterTransition = enterRight,
            popExitTransition = exitRight
        ) { backStackEntry ->
            MoviesScreen(
                viewModel = hiltViewModel(),
                onNavigateToFilterScreen = { currentGenreId ->
                    navController.navigate(Filters(selectedGenreId = currentGenreId))
                }
            )
        }
        composable<Filters>(
            enterTransition = enterLeft,
            exitTransition = exitLeft,
            popEnterTransition = enterRight,
            popExitTransition = exitRight,
        ) {
            FiltersScreen(
                viewModel = hiltViewModel(),
                onFilterSelect = { genreId ->
                    navController.navigate(Movies(genreId = if (genreId == ALL_GENRE_ID) null else genreId)) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

private val enterLeft: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(150))
}

private val exitLeft: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(150))
}

private val enterRight: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(150))
}

private val exitRight: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(150))
}
