package com.example.themovies.ui.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovies.domain.model.Genre
import com.example.themovies.domain.usecase.GetMovieGenresUseCase
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
                        genres = genres
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

    fun onFilterSelect(genreId: Int) {
        // TODO or navigate back with id
    }

    data class State(
        val isLoading: Boolean = true,
        val genres: List<Genre> = emptyList(),
        val hasError: Boolean = false
    )
}
