package com.kymahi.audiobookshelf.android.setup.viewmodels

import android.content.Intent
import android.content.Intent.ACTION_MAIN
import android.content.Intent.CATEGORY_APP_BROWSER
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GitHubViewModel: ViewModel() {

    val openLinkLiveData = MutableLiveData<Intent>()

    fun onServerLinkClick() { openLinkLiveData.value = getIntent(SERVER_URL) }
    fun onAppLinkClick() { openLinkLiveData.value = getIntent(APP_URL) }

    private fun getIntent(url: String) = Intent.makeMainSelectorActivity(ACTION_MAIN, CATEGORY_APP_BROWSER).apply {
        data = Uri.parse(url)
    }

    private companion object {
        const val SERVER_URL = "https://github.com/advplyr/audiobookshelf-app"
        const val APP_URL = "https://github.com/kymahi/Audiobook_Shelf"
    }
}