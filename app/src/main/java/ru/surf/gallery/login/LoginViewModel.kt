package ru.surf.gallery.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _loginStatus = MutableLiveData(NOT_LOGGED_IN)
    val loginStatus: LiveData<Int> = _loginStatus

    fun logInUser(login: String, password: String) {
     // TODO("логин")
        _loginStatus.value = LOGGED_IN
    }

    companion object {
        const val NOT_LOGGED_IN = 0
        const val LOGGED_IN = 1
        const val ERROR = 2
    }
}