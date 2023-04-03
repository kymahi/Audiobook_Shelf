package com.kymahi.audiobookshelf.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.sql.shared.entity.Book
import com.app.sql.shared.entity.Server
import com.app.sql.shared.entity.User
import com.app.sql.shared.cache.Database
import com.app.sql.shared.cache.DatabaseDriverFactory
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

    fun insertServer(server: Server) = db.insertServer(server.toServer())
    fun insertUser(user: User) = db.insertUser(user.toUser())
    fun insertBook(book: Book) = db.insertBook(book.toBook())
    fun getAllServers() = db.getAllServers()
    fun getAllUsers() = db.getAllUsers()
    fun getAllBooks() = db.getAllBooks()
    fun getServerById(id: Int) = db.getServerById(id.toLong())
    fun getUserById(id: Int) = db.getUserById(id.toLong())
    fun getBookById(id: String) = db.getBookById(id)
    fun getServerByUrl(url: String) = db.getServerByUrl(url)
    fun getUserByServer(id: Int) = db.getUserByServer(id.toLong())
    fun removeServer(id: Int) = db.removeServer(id.toLong())
    fun removeUser(id: Int) = db.removeUser(id.toLong())
    fun removeBook(id: String) = db.removeBook(id)
    fun removeAllServers() = db.removeAllServers()
    fun removeAllUsers() = db.removeAllUsers()
    fun removeAllBooks() = db.removeAllBooks()
    fun clearDatabase() = db.clearDatabase()
}
