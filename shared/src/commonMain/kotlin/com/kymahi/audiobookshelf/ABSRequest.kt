package com.kymahi.audiobookshelf

import com.kymahi.audiobookshelf.api.APIPath.PING
import com.kymahi.audiobookshelf.models.PingResult
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.URLProtocol.Companion.HTTP
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.MutableSharedFlow

class ABSRequest {
    val validServerFlow = MutableSharedFlow<String>()
    val invalidServerFlow = MutableSharedFlow<Unit>()

    suspend fun verifyServerAddress(url: String) {
        HttpClient(CIO) {
            install(ContentNegotiation) { json() }
            install(HttpTimeout) { requestTimeoutMillis = 1000L }
            HttpResponseValidator {
                validateResponse {
                    if (it.status.isSuccess() && it.body<PingResult>().isSuccess) {
                        validServerFlow.emit(url.getHost())
                    } else {
                        invalidServerFlow.emit(Unit)
                    }
                }
            }
        }.apply {
            requestAndCatch(
                { get(url.toSafeUrl()) },
                { invalidServerFlow.emit(Unit) }
            )
        }
    }

    private fun HttpStatusCode.isSuccess() = value in 200..299

    private suspend fun <T> HttpClient.requestAndCatch(
        block: suspend HttpClient.() -> T,
        errorHandler: suspend (Throwable) -> T
    ): T = runCatching { block() }.getOrElse { errorHandler(it) }

    private fun String.toSafeUrl() = URLBuilder(HTTP, getHost(), getPort(), pathSegments = getPath().split("/") + PING).build()
    private fun String.getHost() = substringAfter(PROTOCOL).substringBefore(PORT)
    private fun String.getPort() = substringAfter(PROTOCOL).substringAfter(PORT, "").substringBefore(PATH).let {
        if (it.isBlank()) DEFAULT_PORT else try { it.toInt() } catch (t: Throwable) { DEFAULT_PORT }
    }
    private fun String.getPath() = substringAfter(PROTOCOL).substringAfter(PORT).substringAfter(PATH, "")

    private companion object {
        const val PORT = ":"
        const val PATH = "/"
        const val PROTOCOL = "://"
    }
}