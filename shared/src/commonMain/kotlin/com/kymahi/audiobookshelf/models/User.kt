package com.kymahi.audiobookshelf.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id") val id: String,
    @SerialName("username") val username: String,
    @SerialName("type") val type: String,
    @SerialName("token") val token: String
)

@Serializable
data class UserWrapper(@SerialName("user") val user: User)