package com.kymahi.audiobookshelf.android.addserver.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.sql.shared.entity.Server

class AddServerModel: ViewModel() {
    val addServerModalLiveData = MutableLiveData<Unit>()
    val submitDialogLiveData = MutableLiveData<Unit>()
    val cancelDialogLiveData = MutableLiveData<Unit>()
    val errorVisibilityLiveData = MutableLiveData(View.GONE)
    val serverLiveData = MutableLiveData<List<Server>>()

    fun showServerModal() {
        addServerModalLiveData.value = Unit
    }

    fun submit() {
        submitDialogLiveData.value = Unit
    }

    fun cancel() {
        cancelDialogLiveData.value = Unit
    }

    fun showError(isVisible: Boolean) {
        errorVisibilityLiveData.postValue(if (isVisible) View.VISIBLE else View.GONE)
    }

    fun updateServers(servers: List<Server>?) {
        serverLiveData.value = servers ?: emptyList()
    }
}