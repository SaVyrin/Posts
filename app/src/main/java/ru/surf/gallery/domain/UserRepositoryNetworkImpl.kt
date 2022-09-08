package ru.surf.gallery.domain

import retrofit2.Response
import ru.surf.gallery.data.database.*
import ru.surf.gallery.data.network.LoginRequest
import ru.surf.gallery.data.network.LoginResponse
import ru.surf.gallery.data.network.LogoutResponse
import ru.surf.gallery.data.network.NetworkApi
import ru.surf.gallery.ui.login.LoginStatus
import ru.surf.gallery.ui.profile.LogoutStatus
import ru.surf.gallery.utils.toUser
import ru.surf.gallery.utils.toUserToken

class UserRepositoryNetworkImpl(
    private val networkApi: NetworkApi,
    private val userTokenDao: UserTokenDao,
    private val userDao: UserDao,
    private val postDao: PostDao
) : UserRepository {
    override suspend fun login(login: String, password: String): LoginStatus {
        val loginResponse = sendLoginRequest(login, password)
        var loginStatus = LoginStatus.IN_PROGRESS
        when {
            loginResponse.code() == 400 -> {
                loginStatus = LoginStatus.ERROR_WRONG_DATA
            }
            loginResponse.isSuccessful -> {
                loginResponse.body()?.let {
                    addTokenToDb(it.token.toUserToken())
                    addUserToDb(it.userInfo.toUser())
                    loginStatus = LoginStatus.LOGGED_IN
                }
            }
            else -> {
                loginStatus = LoginStatus.ERROR_INTERNET
            }
        }
        return loginStatus
    }

    override suspend fun logout(token: String): LogoutStatus {
        val logoutResponse = sendLogoutRequest(token)
        val logoutStatus = when (logoutResponse.code()) {
            204, 401 -> {
                clearUserData()
                LogoutStatus.LOGGED_OUT
            }
            else -> LogoutStatus.ERROR
        }
        return logoutStatus
    }

    private suspend fun sendLoginRequest(
        login: String,
        password: String
    ): Response<LoginResponse> {
        return networkApi.login(LoginRequest("+7$login", password))
    }

    private suspend fun addTokenToDb(userToken: UserToken) {
        userTokenDao.insert(userToken)
    }

    private suspend fun addUserToDb(user: User) {
        userDao.insert(user)
    }


    private suspend fun sendLogoutRequest(token: String): Response<LogoutResponse> {
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