package com.kymahi.audiobookshelf.android.setup.login.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginModel: ViewModel() {
    val loginLiveData = MutableLiveData<Unit>()
    val loginErrorMessageLiveData = MutableLiveData("")
    val loginErrorVisibilityLiveData = MutableLiveData(View.GONE)

    fun login() {
        loginLiveData.value = Unit
    }

    fun setError(isVisible: Boolean, message: String = "") {
        loginErrorMessageLiveData.postValue(message)
        loginErrorVisibilityLiveData.postValue(if (isVisible) View.VISIBLE else View.GONE)
    }
}