package com.kymahi.audiobookshelf.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jetbrains.handson.kmm.shared.cache.Database
import com.jetbrains.handson.kmm.shared.cache.DatabaseDriverFactory
import comjetbrainshandsonkmmsharedcache.Book
import comjetbrainshandsonkmmsharedcache.Server
import comjetbrainshandsonkmmsharedcache.User
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val db = Database(DatabaseDriverFactory(this))

    override fun onCreate(savedInstanceState: Bundle?) {
        splash()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun splash() {
        var keepSplashScreenOn = true
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                keepSplashScreenOn = false
            }
        }
        installSplashScreen().setKeepOnScreenCondition { keepSplashScreenOn }
        supportActionBar?.hide()
    }

    fun insertServer(server: Server) = db.insertServer(server)
    fun insertUser(user: User) = db.insertUser(user)
    fun insertBook(book: Book) = db.insertBook(book)
    fun getAllServers() = db.getAllServers()
    fun getAllUsers() = db.getAllUsers()
    fun getAllBooks() = db.getAllBooks()
    fun getServerById(id: Int) = db.getServerById(id.toLong())
    fun getUserById(id: Int) = db.getUserById(id.toLong())
    fun getBookById(id: Int) = db.getBookById(id.toLong())
    fun getServerByUrl(url: String) = db.getServerByUrl(url)
    fun getUserByServer(id: Int) = db.getUserByServer(id.toLong())
    fun removeServer(id: Int) = db.removeServer(id.toLong())
    fun removeUser(id: Int) = db.removeUser(id.toLong())
    fun removeBook(id: Int) = db.removeBook(id.toLong())
    fun removeAllServers() = db.removeAllServers()
    fun removeAllUsers() = db.removeAllUsers()
    fun removeAllBooks() = db.removeAllBooks()
    fun clearDatabase() = db.clearDatabase()
}
