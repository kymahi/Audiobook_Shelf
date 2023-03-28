package com.jetbrains.handson.kmm.shared.entity

import comjetbrainshandsonkmmsharedcache.Server
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Server(
    @SerialName("id")
    val id: Int,
    @SerialName("url")
    val url: String
) {
    companion object {
        fun Server.fromCacheServer() = Server(id, url)
    }
}

@Serializable
data class User(
    @SerialName("id")
    val id: Int,
    @SerialName("username")
    val username: String,
    @SerialName("api_token")
    val token: String,
    @SerialName("server_id")
    val serverId: Int
)

@Serializable
data class Book(
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
)