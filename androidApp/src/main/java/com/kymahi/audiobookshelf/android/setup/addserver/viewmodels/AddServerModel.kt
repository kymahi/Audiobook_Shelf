package com.kymahi.audiobookshelf.android.setup.addserver.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.sql.shared.entity.Server

class AddServerModel: ViewModel() {
    val addServerModalLiveData = MutableLiveData<Unit>()
    val submitDialogLiveData = MutableLiveData<Unit>()
    val cancelDialogLiveData = MutableLiveData<Unit>()
    val errorVisibilityLiveData = MutableLiveData(View.GONE)
    val errorTextLiveData = MutableLiveData("")
    val serverLiveData = MutableLiveData<List<Server>>()
    val clearServersLiveData = MutableLiveData<Unit>()
    val onServerClickedLiveData = MutableLiveData<Server>()

    fun showServerModal() {
        addServerModalLiveData.value = Unit
    }

    fun submit() {
        submitDialogLiveData.value = Unit
    }

    fun cancel() {
        cancelDialogLiveData.value = Unit
    }

    fun setError(isVisible: Boolean, errorText: String = "") {
        errorTextLiveData.postValue(errorText)
        errorVisibilityLiveData.postValue(if (isVisible) View.VISIBLE else View.GONE)
    }

    fun updateServers(servers: List<Server>?) {
        serverLiveData.value = servers ?: emptyList()
    }

    private var lastClickTime = 0L
    fun onDoubleClick() {
        lastClickTime = if (System.currentTimeMillis() - lastClickTime < 300){
            clearServersLiveData.value = Unit
            0
        }else{
            System.currentTimeMillis()
        }
    }

    val onItemClick = { url: String, id: Int ->
        onServerClickedLiveData.value = Server(id, url)
    }
}