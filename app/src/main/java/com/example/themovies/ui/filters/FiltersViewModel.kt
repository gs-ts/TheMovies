package com.example.themovies.ui.filters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.themovies.domain.model.Genre
import com.example.themovies.domain.usecase.GetMovieGenresUseCase
import com.example.themovies.ui.Filters
import com.example.themovies.ui.filters.FiltersViewModel.State.Filter.Companion.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMovieGenresUseCase: GetMovieGenresUseCase
) : ViewModel() {

    private val selectedGenreId: Int? = savedStateHandle.toRoute<Filters>().selectedGenreId?.toInt()

    private val _state = MutableStateFlow<State>(State())
    val state: StateFlow<State> = _state.asStateFlow()

    init {
        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            getMovieGenresUseCase()
                .onSuccess { genres ->
                    val filtersList = listOf(
                        State.Filter(
                            id = ALL_GENRE_ID,
                            name = ALL_GENRE_NAME,
                            isSelected = selectedGenreId == null
                        )
                    ) + genres.map { genre ->
                        genre.toUiModel(isSelected = genre.id == selectedGenreId)
                    }

                    _state.value = _state.value.copy(
                        isLoading = false,
                        filters = filtersList
                    )
                }
                .onFailure { exception ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        hasError = true
                    )
                }
        }
    }

    data class State(
        val isLoading: Boolean = true,
        val filters: List<Filter> = emptyList(),
        val hasError: Boolean = false
    ) {

        data class Filter(
            val id: Int,
            val name: String,
            val isSelected: Boolean
        ) {
            companion object {
                fun Genre.toUiModel(isSelected: Boolean): Filter = Filter(
                    id = id,
                    name = name,
                    isSelected = isSelected
                )
            }
        }
    }

    companion object {
        const val ALL_GENRE_ID = -1
        private const val ALL_GENRE_NAME = "All"
    }
}
