package com.example.themovies.data.network

import android.util.Log
import com.example.themovies.domain.model.ConnectionFailed
import com.example.themovies.domain.model.OtherError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.io.IOException
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <reified T> HttpClient.safeRequest(
    block: HttpRequestBuilder.() -> Unit,
): Result<T> =
    try {
        val response = request { block() }
        Result.success(response.body())
    } catch (exception: CancellationException) {
        throw exception // rethrow to propagate cancellation
    } catch (exception: Exception) {
        Log.e("safeRequest", "Network request failed: ${exception.message}", exception)
        val error = when (exception) {
            is IOException,
            is UnresolvedAddressException -> ConnectionFailed

            else -> OtherError
        }
        Result.failure(error)
    }
