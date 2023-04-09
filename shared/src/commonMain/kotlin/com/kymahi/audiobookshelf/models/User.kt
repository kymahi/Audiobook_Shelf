package com.kymahi.audiobookshelf.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.app.sql.shared.entity.MediaProgress as EntityMediaProgress
import com.app.sql.shared.entity.User as EntityUser

@Serializable
data class User(
    @SerialName("id") val id: String,
    @SerialName("username") val username: String,
    @SerialName("token") val token: String,
    @SerialName("mediaProgress") val mediaProgress: List<MediaProgress>
)

@Serializable
data class UserWrapper(
    @SerialName("user") val user: User,
    @SerialName("userDefaultLibraryId") val defaultLibraryId: String
) {
    fun toEntity(serverId: Int) = EntityUser(user.id, user.username, user.token, serverId, defaultLibraryId, user.mediaProgress.map { it.toEntity(user.id) })
}

@Serializable
data class MediaProgress(
    @SerialName("id")
    val id: String,
    @SerialName("libraryItemId")
    val libraryItemId: String,
    @SerialName("episodeId")
    val episodeId: String?,
    @SerialName("duration")
    val duration: Double,
    @SerialName("progress")
    val progress: Double,
    @SerialName("currentTime")
    val currentTime: Double,
    @SerialName("isFinished")
    val isFinished: Boolean,
    @SerialName("hideFromContinueListening")
    val hideFromContinueListening: Boolean,
    @SerialName("lastUpdate")
    val lastUpdate: Long,
    @SerialName("startedAt")
    val startedAt: Long,
    @SerialName("finishedAt")
    val finishedAt: Long?
) {
    fun toEntity(userId: String) = EntityMediaProgress(id, libraryItemId, episodeId, duration, progress, currentTime, isFinished, hideFromContinueListening, lastUpdate, startedAt, finishedAt, userId)
}