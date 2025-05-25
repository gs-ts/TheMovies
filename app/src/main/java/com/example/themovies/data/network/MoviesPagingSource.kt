package com.example.themovies.data.network

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.themovies.data.dto.MovieListItemDto
import com.example.themovies.domain.model.ConnectionFailed
import com.example.themovies.domain.model.OtherError
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.io.IOException

class MoviesPagingSource(
    private val tmdbApi: TmdbApi,
    private val genreId: Int? = null
) : PagingSource<Int, MovieListItemDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieListItemDto> {
        val pageNumber = params.key ?: 1

        return try {
            val moviesDto = tmdbApi.getMovies(
                page = pageNumber,
                genreId = genreId
            ).results.orEmpty()

            LoadResult.Page(
                data = moviesDto,
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if (moviesDto.isEmpty()) null else pageNumber + 1
            )
        } catch (exception: Exception) {
            Log.e("MoviesPagingSource", "exception: " + exception.message)
            val error = when (exception) {
                is IOException,
                is TimeoutCancellationException,
                is UnresolvedAddressException -> ConnectionFailed

                else -> OtherError
            }
            LoadResult.Error(error)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieListItemDto>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}
