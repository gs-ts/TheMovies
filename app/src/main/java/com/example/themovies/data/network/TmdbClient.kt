package com.example.themovies.data.network

import com.example.themovies.data.dto.GenreListResponse
import com.example.themovies.data.dto.MovieDetailsDto
import com.example.themovies.data.dto.MovieListResponse
import com.example.themovies.domain.model.Genre
import com.example.themovies.domain.model.MovieDetails
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import io.ktor.http.appendPathSegments
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TmdbClient @Inject constructor(private val httpClient: HttpClient) : TmdbApi {

    override suspend fun getMovies(
        page: Int,
        genreId: Int?
    ): MovieListResponse {
        return httpClient.get(TMDB_ENDPOINT_MOVIES_LIST) {
            parameter(PAGE_PARAMETER, page)
            parameter(GENRE_PARAMETER, genreId)
        }.body()
    }

    override suspend fun getGenres(): Result<List<Genre>> {
        return httpClient.safeRequest<GenreListResponse> {
            method = HttpMethod.Get
            url {
                appendPathSegments(TMDB_ENDPOINT_MOVIE_GENRES)
            }
        }.mapCatching { genreListResponse ->
            genreListResponse.genres.map { genre ->
                genre.toDomainModel()
            }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetails> {
        return httpClient.safeRequest<MovieDetailsDto> {
            method = HttpMethod.Get
            url {
                appendPathSegments(TMDB_ENDPOINT_MOVIE_DETAILS, movieId.toString())
            }
        }.mapCatching { movieDetailsDto ->
            movieDetailsDto.toDomainModel()
        }
    }

    companion object {
        private const val TMDB_ENDPOINT_MOVIE_GENRES = "genre/movie/list"
        private const val TMDB_ENDPOINT_MOVIES_LIST = "discover/movie"
        private const val TMDB_ENDPOINT_MOVIE_DETAILS = "movie"

        private const val PAGE_PARAMETER = "page"
        private const val GENRE_PARAMETER = "with_genres"
    }
}
