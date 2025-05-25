package com.example.themovies.di

object TmdbApiConstants {
    const val BASE_URL = "https://api.themoviedb.org/3/"

    const val TMDB_API_KEY = "YOUR_API_KEY_HERE"

    const val TIMEOUT_30_SECONDS: Long = 30 * 1000
    const val MAX_RETRIES: Int = 3
}
