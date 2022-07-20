package ru.surf.gallery.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.surf.gallery.database.PostDao
import ru.surf.gallery.database.UserDao
import ru.surf.gallery.database.UserToken
import ru.surf.gallery.database.UserTokenDao
import ru.surf.gallery.rest.PostApi

class ProfileViewModel(
    val userTokenDao: UserTokenDao,
    val userDao: UserDao,
    val postDao: PostDao
) : ViewModel() {

    private val mutableLogoutStatus = MutableLiveData(LogoutStatus.NOT_LOGGED_OUT)
    val logoutStatus: LiveData<LogoutStatus> = mutableLogoutStatus

    val user = userDao.getAll()
    val userToken = userTokenDao.getAll()
    private var token = ""

    fun setUserToken(newUserToken: UserToken) {
        token = newUserToken.token
    }

    fun logOutUser() {
        viewModelScope.launch {
            try {
                mutableLogoutStatus.value = LogoutStatus.IN_PROGRESS
                val postApi = PostApi.create()
                val logoutResponse = postApi.logout("Token $token")
                when (logoutResponse.code()) {
                    204, 401 -> {
                        clearUserData()
                        mutableLogoutStatus.value = LogoutStatus.LOGGED_OUT
                    }
                    else -> Throwable()
                }
            } catch (error: Throwable) {
                mutableLogoutStatus.value = LogoutStatus.ERROR
            }
        }
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