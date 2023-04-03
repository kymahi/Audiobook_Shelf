package com.app.sql.shared.entity

expect class Server(id: Int, url: String)

expect class User(id: Int, username: String, token: String, serverId: Int)

expect class Book(id: String, title: String, subtitle: String, authors: String, series: String, narrators: String, duration: Int, genres: String, published: Int, description: String, progress: Int, bookmarks: String, serverUrl: String)