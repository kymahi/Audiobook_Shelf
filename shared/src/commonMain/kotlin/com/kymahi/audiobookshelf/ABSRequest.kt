package com.kymahi.audiobookshelf

import com.app.sql.shared.entity.User
import com.kymahi.audiobookshelf.api.APIPath.LOGIN
import com.kymahi.audiobookshelf.api.APIPath.PASSWORD
import com.kymahi.audiobookshelf.api.APIPath.PING
import com.kymahi.audiobookshelf.api.APIPath.USERNAME
import com.kymahi.audiobookshelf.models.PingResult
import com.kymahi.audiobookshelf.models.UserWrapper
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.http.URLProtocol.Companion.HTTP
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.json.Json

class ABSRequest {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) { requestTimeoutMillis = 10_000L }
        install(HttpRedirect) { checkHttpMethod = false }
    }

    val validServerFlow = MutableSharedFlow<String>()
    val invalidServerFlow = MutableSharedFlow<Unit>()
    val validLoginFlow = MutableSharedFlow<User>()
    val invalidLoginFlow = MutableSharedFlow<Unit>()

    suspend fun verifyServerAddress(url: String) {
        val verifyServer = client.config {
            HttpResponseValidator {
                validateResponse {
                    if (it.status.isSuccess() && it.body<PingResult>().isSuccess) {
                        validServerFlow.emit(url.getHost())
                    } else {
                        invalidServerFlow.emit(Unit)
                    }
                }
            }
        }

        verifyServer.requestAndCatch(
            { post(url.toSafeUrl(PING)) },
            { invalidServerFlow.emit(Unit) }
        )

        verifyServer.close()
    }

    suspend fun login(url: String, username: String, password: String, serverId: Int) {
        val login = client.config {
            HttpResponseValidator {
                validateResponse {
                    if (it.status.isSuccess()) {
                        validLoginFlow.emit(it.body<UserWrapper>().toEntity(serverId))
                    } else {
                        invalidLoginFlow.emit(Unit)
                    }
                }
            }
        }
        val parameters = Parameters.build {
            append(USERNAME, username)
            append(PASSWORD, password)
        }

        login.requestAndCatch(
            { submitForm(url.toSafeUrl(LOGIN).toString(), parameters) },
            { invalidLoginFlow.emit(Unit) }
        )

        login.close()
    }

    private fun HttpStatusCode.isSuccess() = value in 200..299

    private suspend fun <T> HttpClient.requestAndCatch(
        block: suspend HttpClient.() -> T,
        errorHandler: suspend (Throwable) -> T
    ): T = runCatching { block() }.getOrElse { errorHandler(it) }

    companion object {
        fun String.toSafeUrl(path: String, parameters: Parameters = Parameters.Empty) = URLBuilder(
            HTTP,
            getHost(),
            getPort(),
            pathSegments = getPath().split("/") + path,
            parameters = parameters
        ).build()

        fun String.getHost() = substringAfter(PROTOCOL).substringBefore(PORT)
        fun String.getPort() = substringAfter(PROTOCOL).substringAfter(PORT, "").substringBefore(PATH).let {
            if (it.isBlank()) DEFAULT_PORT else try {
                it.toInt()
            } catch (t: Throwable) {
                DEFAULT_PORT
            }
        }

        fun String.getPath() = substringAfter(PROTOCOL).substringAfter(PORT).substringAfter(PATH, "")

        const val PORT = ":"
        const val PATH = "/"
        const val PROTOCOL = "://"
    }
}