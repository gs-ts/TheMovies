package com.example.themovies.data.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.serialization.JsonConvertException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

suspend inline fun <reified T> HttpClient.safeRequest(
    block: HttpRequestBuilder.() -> Unit,
): Result<T> =
    try {
        val response = request { block() }
        Result.success(response.body())
    }
//    catch (e: CancellationException) { // TODO
//        throw e // rethrow to not interfere with coroutine cancellation
//    }
    catch (e: JsonConvertException) {
        Log.e("SafeRequest", "JsonConvert Error: ${e.message}", e)
        Result.failure(e)
    } catch (e: ClientRequestException) {
        Log.e("SafeRequest", "Client Error: ${e.response.status.value}", e)
        Result.failure(e)
    } catch (e: ServerResponseException) {
        Log.e("SafeRequest", "Server Error: ${e.response.status.value}", e)
        Result.failure(e)
    } catch (e: IOException) {
        Log.e("SafeRequest", "Network Error", e)
        Result.failure(e)
    } catch (e: SerializationException) {
        Log.e("SafeRequest", "Serialization Error", e)
        Result.failure(e)
    }
