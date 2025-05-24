package com.example.themovies.domain.model

sealed class NetworkError : Exception() {
    data object ConnectionFailed : NetworkError()
    data object OtherError : NetworkError()
}
