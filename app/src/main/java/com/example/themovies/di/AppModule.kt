package com.example.themovies.di

import com.example.themovies.data.network.TmdbApi
import com.example.themovies.data.network.TmdbClient
import com.example.themovies.data.repository.MoviesRepositoryImpl
import com.example.themovies.di.TmdbApiConstants.BASE_URL
import com.example.themovies.di.TmdbApiConstants.TMDB_API_KEY
import com.example.themovies.domain.repository.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }

            install(HttpTimeout) {
                requestTimeoutMillis = TmdbApiConstants.TIMEOUT_30_SECONDS
                connectTimeoutMillis = TmdbApiConstants.TIMEOUT_30_SECONDS
                socketTimeoutMillis = TmdbApiConstants.TIMEOUT_30_SECONDS
            }

            install(HttpRequestRetry) {
                maxRetries = TmdbApiConstants.MAX_RETRIES
                exponentialDelay()
            }

            install(DefaultRequest) {
                url(BASE_URL)
                header(HttpHeaders.Accept, "application/json")
                header(HttpHeaders.Authorization, "Bearer $TMDB_API_KEY")
            }
        }
    }

    @Singleton
    @Provides
    fun provideTmdbApi(httpClient: HttpClient): TmdbApi = TmdbClient(httpClient)

    @Provides
    fun provideMoviesRepository(tmdbApi: TmdbApi): MoviesRepository = MoviesRepositoryImpl(tmdbApi)
}
