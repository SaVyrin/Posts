package ru.surf.gallery.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.surf.gallery.database.UserToken
import ru.surf.gallery.database.UserTokenDao

class SplashViewModel(userTokenDao: UserTokenDao) : ViewModel() {

    val userToken = userTokenDao.getAll()
    val isLoggedIn = MutableLiveData(NOT_LOGGED_IN)

    fun setLoginStatus(tokenList: List<UserToken>) {
        when (tokenList.isEmpty()) {
            true -> isLoggedIn.value = NOT_LOGGED_IN
            false -> isLoggedIn.value = LOGGED_IN
        }
    }

    // TODO можно заменить на sealed class
    companion object {
        const val NOT_LOGGED_IN = false
        const val LOGGED_IN = true
    }
}