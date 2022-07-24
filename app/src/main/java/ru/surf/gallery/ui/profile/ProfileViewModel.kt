package ru.surf.gallery.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.surf.gallery.data.database.PostDao
import ru.surf.gallery.data.database.UserDao
import ru.surf.gallery.data.database.UserToken
import ru.surf.gallery.data.database.UserTokenDao
import ru.surf.gallery.data.network.LogoutResponse
import ru.surf.gallery.data.network.NetworkApi
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userTokenDao: UserTokenDao,
    private val userDao: UserDao,
    private val postDao: PostDao,
    private val networkApi: NetworkApi
) : ViewModel() {

    private val mutableLogoutStatus = MutableLiveData(LogoutStatus.NOT_LOGGED_OUT)
    val logoutStatus: LiveData<LogoutStatus> = mutableLogoutStatus

    val user = userDao.get()
    val userToken = userTokenDao.get()
    private var token = ""

    fun setUserToken(newUserToken: UserToken) {
        token = newUserToken.token
    }

    fun logOutUser() {
        viewModelScope.launch {
            try {
                mutableLogoutStatus.value = LogoutStatus.IN_PROGRESS
                val logoutResponse = sendLogoutRequest()
                when (logoutResponse.code()) {
                    204, 401 -> {
                        clearUserData()
                        mutableLogoutStatus.value = LogoutStatus.LOGGED_OUT
                    }
                    else -> mutableLogoutStatus.value = LogoutStatus.ERROR
                }
            } catch (error: Throwable) {
                error.printStackTrace()
                mutableLogoutStatus.value = LogoutStatus.ERROR
            }
        }
    }

    private suspend fun sendLogoutRequest(): Response<LogoutResponse> {
        return networkApi.logout("Token $token")
    }

    private suspend fun clearUserData() {
        removeUserTokenFromDb()
        removeUserInfoFromDb()
        removePostsFromDb()
    }

    private suspend fun removeUserTokenFromDb() {
        userTokenDao.deleteAll()
    }

    private suspend fun removeUserInfoFromDb() {
        userDao.deleteAll()
    }

    private suspend fun removePostsFromDb() {
        postDao.deleteAll()
    }
}