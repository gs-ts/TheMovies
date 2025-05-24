package com.example.themovies.di

object TmdbApiConstants {
    const val BASE_URL = "https://api.themoviedb.org/3/"

    const val TMDB_API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlMWQ2NTFiYWNmMGQ5ZDY0NDc1ZjVkZTViMmE4MmIxZCIsIm5iZiI6MTY0MTczOTA1MC40Njg5OTk5LCJzdWIiOiI2MWRhZjMyYWVmZWE3YTAwNDI2M2VhZDUiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.cE6BgJC_nT4YEPjbTFZG7hEaQAmFQfXSzyls3k2uuB0"

    const val TIMEOUT_30_SECONDS: Long = 30 * 1000
    const val MAX_RETRIES: Int = 3
}
