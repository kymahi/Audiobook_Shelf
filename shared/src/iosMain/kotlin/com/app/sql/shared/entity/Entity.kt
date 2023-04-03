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
    companion object {
        fun Server.toServer() = Server(id.toInt(), url)
    }
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
    companion object {
        fun User.toUser() = User(id, username, api_token, serverId)
    }
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
    companion object {
        fun Book.toBook() = Book(id.toString(), title, subtitle, authors, series, narrators, duration.toInt(), genres, published_year.toInt(), description, progress.toInt(), bookmarks, url)
    }
}