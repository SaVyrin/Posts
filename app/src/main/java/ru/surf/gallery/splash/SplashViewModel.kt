package ru.surf.gallery.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.surf.gallery.database.UserToken
import ru.surf.gallery.database.UserTokenDao
import ru.surf.gallery.login.LoginStatus

class SplashViewModel(
    userTokenDao: UserTokenDao
) : ViewModel() {

    val userToken = userTokenDao.getAll()
    private val mutableLogInStatus = MutableLiveData(LoginStatus.NOT_LOGGED_IN)
    val logInStatus: LiveData<LoginStatus> = mutableLogInStatus

    fun setLoginStatus(tokenList: List<UserToken>) {
        when (tokenList.isEmpty()) {
            true -> mutableLogInStatus.value = LoginStatus.NOT_LOGGED_IN
            false -> mutableLogInStatus.value = LoginStatus.LOGGED_IN
        }
    }

}