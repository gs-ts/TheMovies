package com.example.themovies.ui.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovies.domain.model.Genre
import com.example.themovies.domain.usecase.GetMovieGenresUseCase
import com.example.themovies.ui.filters.FiltersViewModel.State.Filter.Companion.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val getMovieGenresUseCase: GetMovieGenresUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State())
    val state: StateFlow<State> = _state.asStateFlow()

    init {
        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            getMovieGenresUseCase()
                .onSuccess { genres ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        filters = genres.map { genre ->
                            genre.toUiModel(isSelected = false)
                        }
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
}
