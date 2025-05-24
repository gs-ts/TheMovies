package com.example.themovies

import com.example.themovies.data.network.TmdbClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertTrue

class TmdbClientTest {

    @Test
    fun `given a request for genres when network succeeds then TmdbClient returns result success`() =
        runTest {
            val mockEngine = MockEngine { request ->
                respond(
                    content = """
            {
              "genres": [
                {
                  "id": 28,
                  "name": "Action"
                },
                {
                  "id": 12,
                  "name": "Adventure"
                },
                {
                  "id": 16,
                  "name": "Animation"
                },
                {
                  "id": 35,
                  "name": "Comedy"
                },
                {
                  "id": 80,
                  "name": "Crime"
                }
              ]
            }
                    """.trimIndent(),
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }

            val client = HttpClient(mockEngine) {
                install(ContentNegotiation) {
                    json(Json { ignoreUnknownKeys = true })
                }
            }

            val tmdbClient = TmdbClient(client)
            val result = tmdbClient.getGenres()

            assertTrue(result.isSuccess)
            assertEquals(5, result.getOrThrow().genres.size)
            assertEquals(35, result.getOrThrow().genres[3].id)
        }

    @Test
    fun `given a request for genres when network fails then TmdbClient returns result failure`() =
        runTest {
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
