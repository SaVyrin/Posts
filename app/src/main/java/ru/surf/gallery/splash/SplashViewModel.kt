package ru.surf.gallery.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.surf.gallery.database.UserTokenDao
import ru.surf.gallery.login.LoginStatus
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    userTokenDao: UserTokenDao
) : ViewModel() {

    val userToken = userTokenDao.get()
    private val mutableLogInStatus = MutableLiveData(LoginStatus.NOT_LOGGED_IN)
    val logInStatus: LiveData<LoginStatus> = mutableLogInStatus

    fun setLoginStatus() {
        mutableLogInStatus.value = LoginStatus.LOGGED_IN
    }
}