package com.jetbrains.handson.kmm.shared.cache

import comjetbrainshandsonkmmsharedcache.Book
import comjetbrainshandsonkmmsharedcache.Server
import comjetbrainshandsonkmmsharedcache.User

class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    fun getAllServers() = dbQuery.selectAllServers().executeAsList()
    fun getAllUsers() = dbQuery.selectAllUsers().executeAsList()
    fun getAllBooks() = dbQuery.selectAllBooks().executeAsList()
    fun getServerById(id: Long) = dbQuery.transaction { dbQuery.selectServerById(id) }
    fun getUserById(id: Long) = dbQuery.transaction { dbQuery.selectUserById(id) }
    fun getBookById(id: Long) = dbQuery.transaction { dbQuery.selectBookById(id) }
    fun getServerByUrl(url: String) = dbQuery.transaction { dbQuery.selectServerByUrl(url) }
    fun getUserByServer(id: Long) = dbQuery.transaction { dbQuery.selectUserByServer(id) }
    fun removeAllServers() = dbQuery.transaction { dbQuery.removeAllServers() }
    fun removeAllUsers() = dbQuery.transaction { dbQuery.removeAllUsers() }
    fun removeAllBooks() = dbQuery.transaction { dbQuery.removeAllBooks() }
    fun removeServer(id: Long) = dbQuery.transaction { dbQuery.removeServerById(id) }
    fun removeUser(id: Long) = dbQuery.transaction { dbQuery.removeUserById(id) }
    fun removeBook(id: Long) = dbQuery.transaction { dbQuery.removeBookById(id) }
    fun insertServer(server: Server) = dbQuery.transaction {
        dbQuery.insertServer(null, server.url)
    }

    fun insertUser(user: User) = dbQuery.transaction {
        dbQuery.insertUser(null, user.username, user.api_token, user.serverId)
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
}