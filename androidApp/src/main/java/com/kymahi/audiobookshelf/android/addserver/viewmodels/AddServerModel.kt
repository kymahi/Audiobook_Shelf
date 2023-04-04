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
    val clearServersLiveData = MutableLiveData<Unit>()

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

    private var lastClickTime = 0L
    fun onDoubleClick() {
        lastClickTime = if (System.currentTimeMillis() - lastClickTime < 300){
            clearServersLiveData.value = Unit
            0
        }else{
            System.currentTimeMillis()
        }
    }
}