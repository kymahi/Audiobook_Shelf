package com.kymahi.audiobookshelf

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform