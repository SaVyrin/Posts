package ru.surf.gallery.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.surf.gallery.database.User
import ru.surf.gallery.database.UserDao
import ru.surf.gallery.rest.LoginRequest
import ru.surf.gallery.rest.PostApi

class LoginViewModel(val userDao: UserDao) : ViewModel() {

    private val _loginStatus = MutableLiveData(NOT_LOGGED_IN)
    val loginStatus: LiveData<Int> = _loginStatus

    suspend fun logInUser(login: String, password: String) {
        viewModelScope.launch {
            val postApi = PostApi.create()
            val loginReq =
                async {
                    postApi.login(
                        LoginRequest(
                            "+71234567890",
                            "qwerty"
                        )
                    )
                }.await()
            addTask(loginReq.userInfo)
            Log.e("Request", "${loginReq.token}")
        }
        _loginStatus.value = LOGGED_IN
    }

    fun addTask(user: User) {
        viewModelScope.launch {
            userDao.insert(user)
            Log.e("Request", "SUCCESS")
        }
    }

    companion object {
        const val NOT_LOGGED_IN = 0
        const val LOGGED_IN = 1
        const val ERROR = 2
    }
}