package com.example.themovies

import androidx.paging.PagingSource
import com.example.themovies.FakeTmdbApi.Companion.mockMovieItems
import com.example.themovies.data.dto.GenreListResponse
import com.example.themovies.data.dto.MovieDetailsDto
import com.example.themovies.data.dto.MovieListItemDto
import com.example.themovies.data.dto.MovieListResponse
import com.example.themovies.data.network.MoviesPagingSource
import com.example.themovies.data.network.TmdbApi
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import org.junit.Test

class MoviesPagingSourceTest {

    @Test
    fun `paging source returns correct page with pagination`() = runTest {
        val pagingSource = MoviesPagingSource(tmdbApi = FakeTmdbApi())

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        val page = result as PagingSource.LoadResult.Page

        assertEquals(mockMovieItems, page.data)
        assertEquals(null, page.prevKey)
        assertEquals(2, page.nextKey)
    }

    @Test
    fun `paging source returns error when exception is thrown`() = runTest {
        val errorSource = MoviesPagingSource(FakeTmdbApi(withError = true))

        val result = errorSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Error)
    }
}

class FakeTmdbApi(private val withError: Boolean = false) : TmdbApi {

    companion object {
        val mockMovieItems = listOf(
            MovieListItemDto(
                id = 1,
                posterPath = null,
                title = "Movie 1",
                rating = 8.5
            ),
            MovieListItemDto(
                id = 2,
                posterPath = null,
                title = "Movie 2",
                rating = 7.2
            ),
            MovieListItemDto(
                id = 3,
                posterPath = null,
                title = "Movie 3",
                rating = 6.8
            ),
            MovieListItemDto(
                id = 4,
                posterPath = null,
                title = "Movie 4",
                rating = 9.1
            ),
            MovieListItemDto(
                id = 5,
                posterPath = null,
                title = "Movie 5",
                rating = 5.5
            )
        )
    }

    override suspend fun getMovies(
        page: Int,
        genreId: Int?
    ): MovieListResponse {
        if (withError) throw IOException("some error")

        return MovieListResponse(
            page = 1,
            results = mockMovieItems,
            totalPages = 3,
            totalResults = 5
        )
    }

    override suspend fun getGenres(): Result<GenreListResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsDto> {
        TODO("Not yet implemented")
    }
}
