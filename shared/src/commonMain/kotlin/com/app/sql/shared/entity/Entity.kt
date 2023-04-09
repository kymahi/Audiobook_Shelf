package com.app.sql.shared.entity

expect class Server(id: Int, url: String)

expect class User(id: String, username: String, token: String, serverId: Int, defaultLibrary: String, mediaProgress: List<MediaProgress>)

expect class MediaProgress(id: String, libraryItemId: String, episodeId: String?, duration: Double, progress: Double, currentTime: Double, isFinished: Boolean, hideFromContinueListening: Boolean, lastUpdate: Long, startedAt: Long, finishedAt: Long?, userId: String)

expect class Book(id: String, title: String, subtitle: String, authors: String, series: String, narrators: String, duration: Int, genres: String, published: Int, description: String, progress: Int, bookmarks: String, serverUrl: String)