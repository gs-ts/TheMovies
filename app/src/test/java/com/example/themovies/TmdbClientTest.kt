package com.example.themovies

import com.example.themovies.data.network.TmdbClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import org.junit.Test
import kotlin.test.assertTrue

class TmdbClientTest {

    @Test
    fun `TmdbClient returns result failure when network fails`() = runTest {
        val client = HttpClient(MockEngine) {
            engine {
                addHandler {
                    throw IOException("some network error")
                }
            }
            install(ContentNegotiation) {
                json()
            }
        }

        val tmdbClient = TmdbClient(client)
        val result = tmdbClient.getGenres()

        assertTrue(result.isFailure)
        assertEquals(
            "some network error",
            result.getOrElse {
                it.message
            }
        )
    }
}
