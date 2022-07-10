package ru.surf.gallery.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    var loginStatus = NOT_LOGGED_IN

    fun logInUser(login: String, password: String) {
     // TODO("логин")
        loginStatus = LOGGED_IN
    }

    companion object {
        const val NOT_LOGGED_IN = 0
        const val LOGGED_IN = 1
        const val ERROR = 2
    }
}