package com.example.themovies.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.themovies.data.dto.MovieListItemDto
import io.ktor.client.plugins.ClientRequestException
import kotlinx.io.IOException

class MoviesPagingSource(
    private val tmdbApi: TmdbApi,
    private val genreId: Int?
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
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: ClientRequestException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieListItemDto>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}
