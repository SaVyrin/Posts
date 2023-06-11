package ru.surf.gallery.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.surf.gallery.data.database.User
import ru.surf.gallery.data.database.UserDao
import ru.surf.gallery.data.database.UserToken
import ru.surf.gallery.data.database.UserTokenDao
import ru.surf.gallery.domain.UserRepository
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ProfileViewModel @Inject constructor(
    userTokenDao: UserTokenDao,
    val userDao: UserDao,
    @Named("vsu_user") private val userRepository: UserRepository
) : ViewModel() {

    private val mutableLogoutStatus = MutableLiveData(LogoutStatus.NOT_LOGGED_OUT)
    val logoutStatus: LiveData<LogoutStatus> = mutableLogoutStatus

    val user = MutableLiveData<User>()
    val userToken = userTokenDao.get()
    private var token = ""

    fun setUserToken(newUserToken: UserToken) {
        token = newUserToken.token
        viewModelScope.launch {
            user.value = userDao.getById(token)
        }
    }

    fun logOutUser() {
        viewModelScope.launch {
            try {
                mutableLogoutStatus.value = LogoutStatus.IN_PROGRESS
                val newLogoutStatus = userRepository.logout(token)
                mutableLogoutStatus.value = newLogoutStatus
            } catch (error: Throwable) {
                error.printStackTrace()
                mutableLogoutStatus.value = LogoutStatus.ERROR
            }
        }
    }
}