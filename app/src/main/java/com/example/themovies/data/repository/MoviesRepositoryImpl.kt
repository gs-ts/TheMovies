package com.example.themovies.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.themovies.data.network.MoviesPagingSource
import com.example.themovies.data.network.TmdbApi
import com.example.themovies.domain.model.Genre
import com.example.themovies.domain.model.Movie
import com.example.themovies.domain.model.MovieDetails
import com.example.themovies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi
) : MoviesRepository {

    override fun getMovies(
        page: Int,
        genreId: Int?
    ): Flow<PagingData<Movie>> {
        return Pager(
            PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 5
            )
        ) {
            MoviesPagingSource(tmdbApi, genreId)
        }.flow
            .map { pagingData ->
                pagingData.map { movieListItemDto ->
                    movieListItemDto.toDomainModel()
                }
            }
    }

    override suspend fun getGenres(): Result<List<Genre>> {
        return tmdbApi.getGenres()
            .mapCatching { genreListResponse ->
                genreListResponse.genres.map { genre ->
                    genre.toDomainModel()
                }
            }
    }

    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetails> {
        return tmdbApi.getMovieDetails(movieId = movieId)
            .mapCatching { movieDetailsDto ->
                movieDetailsDto.toDomainModel()
            }
    }
}
