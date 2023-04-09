package com.app.sql.shared.cache

import com.app.sql.shared.AppDatabase
import com.app.sql.shared.Book
import com.app.sql.shared.Server
import com.app.sql.shared.User
import com.app.sql.shared.entity.MediaProgress as PlatformMediaProgress
import com.app.sql.shared.entity.Server as PlatformServer
import com.app.sql.shared.entity.User as PlatformUser
import com.app.sql.shared.entity.Book as PlatformBook

class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    fun getAllServers() = dbQuery.selectAllServers(::mapServer).executeAsList()
    fun getServerById(id: Long) = dbQuery.transactionWithResult { dbQuery.selectServerById(id, ::mapServer) }.executeAsOneOrNull()
    fun getUserById(id: String) = dbQuery.transactionWithResult {
        val mediaProgress = dbQuery.selectMediaProgressByUserId(id, ::mapMediaProgress).executeAsList()
        val user = dbQuery.selectUserById(id).executeAsOne()
        mapUser(user.id, user.username, user.api_token, user.serverId, user.default_library, mediaProgress)
    }
    fun getBookById(id: String) = dbQuery.transactionWithResult { dbQuery.selectBookById(id, ::mapBook) }.executeAsOneOrNull()
    fun getServerByUrl(url: String) = dbQuery.transactionWithResult { dbQuery.selectServerByUrl(url, ::mapServer) }.executeAsOneOrNull()
    fun getUserByServer(id: Long) = dbQuery.transactionWithResult {
        dbQuery.selectUserByServer(id).executeAsOneOrNull()?.let {
            val mediaProgress = dbQuery.selectMediaProgressByUserId(it.id, ::mapMediaProgress).executeAsList()
            mapUser(it.id, it.username, it.api_token, it.serverId, it.default_library, mediaProgress)
        }
    }
    fun removeAllServers() = dbQuery.transaction { dbQuery.removeAllServers() }
    fun removeAllUsers() = dbQuery.transaction { dbQuery.removeAllUsers() }
    fun removeAllBooks() = dbQuery.transaction { dbQuery.removeAllBooks() }
    fun removeServer(id: Long) = dbQuery.transaction { dbQuery.removeServerById(id) }
    fun removeUser(id: String) = dbQuery.transaction { dbQuery.removeUserById(id) }
    fun removeBook(id: String) = dbQuery.transaction { dbQuery.removeBookById(id) }
    fun insertServer(server: Server) = dbQuery.transaction {
        dbQuery.insertServer(null, server.url)
    }

    fun insertUser(user: User, serverId: Long) = dbQuery.transaction {
        dbQuery.insertUser(user.id, user.username, user.api_token, user.default_library, serverId)
    }

    fun insertBook(book: Book) = dbQuery.transaction {
        dbQuery.insertBook(
            book.id,
            book.title,
            book.subtitle,
            book.authors,
            book.series,
            book.narrators,
            book.duration,
            book.genres,
            book.published_year,
            book.description,
            book.progress,
            book.bookmarks,
            book.url
        )
    }

    fun clearDatabase() = dbQuery.transaction {
        dbQuery.removeAllServers()
        dbQuery.removeAllUsers()
        dbQuery.removeAllBooks()
    }

    private fun mapServer(id: Long, url: String): PlatformServer = PlatformServer(id.toInt(), url)
    private fun mapUser(id: String, username: String, token: String, serverId: Long, defaultLibrary: String, mediaProgress: List<PlatformMediaProgress>): PlatformUser = PlatformUser(id, username, token, serverId.toInt(), defaultLibrary, mediaProgress)
    private fun mapMediaProgress(id: String, libraryItemId: String, episodeId: String?, duration: Double, progress: Double, currentTime: Double, isFinished: Long, hideFromContinueListening: Long, lastUpdate: Long, startedAt: Long, finishedAt: Long?, userId: String) = PlatformMediaProgress(id, libraryItemId, episodeId, duration, progress, currentTime, isFinished.toBoolean(), hideFromContinueListening.toBoolean(), lastUpdate, startedAt, finishedAt, userId)
    private fun mapBook(id: String, title: String, subtitle: String, authors: String, series: String, narrators: String, duration: Long, genres: String, published: Long, description: String, progress: Long, bookmarks: String, serverUrl: String): PlatformBook = PlatformBook(id, title, subtitle, authors, series, narrators, duration.toInt(), genres, published.toInt(), description, progress.toInt(), bookmarks, serverUrl)

    private fun Long.toBoolean() = equals(1)
}