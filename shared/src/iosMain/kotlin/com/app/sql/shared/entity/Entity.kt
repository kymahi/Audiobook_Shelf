package com.app.sql.shared.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.app.sql.shared.Book
import com.app.sql.shared.Server
import com.app.sql.shared.User

@Serializable
actual data class Server actual constructor(
    @SerialName("id")
    val id: Int,
    @SerialName("url")
    val url: String
) {
    fun toServer() = Server(id.toLong(), url)
}

@Serializable
actual data class User actual constructor(
    @SerialName("id")
    val id: Int,
    @SerialName("username")
    val username: String,
    @SerialName("api_token")
    val token: String,
    @SerialName("server_id")
    val serverId: Int
) {
    fun toUser() = User(id.toLong(), username, token, serverId.toLong())
}

@Serializable
actual data class Book actual constructor(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("subtitle")
    val subtitle: String,
    @SerialName("authors")
    val authors: String,
    @SerialName("series")
    val series: String,
    @SerialName("narrators")
    val narrators: String,
    @SerialName("duration")
    val duration: Int,
    @SerialName("genres")
    val genres: String,
    @SerialName("published_year")
    val published: Int,
    @SerialName("description")
    val description: String,
    @SerialName("progress")
    val progress: Int,
    @SerialName("bookmarks")
    val bookmarks: String,
    @SerialName("url")
    val serverUrl: String
) {
    fun toBook() = Book(id, title, subtitle, authors, series, narrators, duration.toLong(), genres, published.toLong(), description, progress.toLong(), bookmarks, serverUrl)
}