package ru.surf.gallery.domain

import retrofit2.Response
import ru.surf.gallery.data.database.*
import ru.surf.gallery.data.network.LoginRequest
import ru.surf.gallery.data.network.LoginResponse
import ru.surf.gallery.data.network.LogoutResponse
import ru.surf.gallery.data.network.NetworkApi
import ru.surf.gallery.ui.login.LoginStatus
import ru.surf.gallery.ui.profile.LogoutStatus


class UserRepositoryVSUImpl(
    private val networkApi: NetworkApi,
    private val userTokenDao: UserTokenDao,
    private val userDao: UserDao,
    private val postDao: PostDao
) : UserRepository {
    override suspend fun login(login: String, password: String): LoginStatus {
        val userWithSameLogin = userDao.getByLogin("+7$login")
        val userId = userWithSameLogin.id
        addTokenToDb(UserToken(userId))
        return LoginStatus.LOGGED_IN
    }

    override suspend fun logout(token: String): LogoutStatus {
        clearUserData()
        return LogoutStatus.LOGGED_OUT
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

    override suspend fun clearUserData() {
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
        postDao.deleteAllPosts()
        postDao.deleteAllUserPosts()
        postDao.deleteAllFeaturedPosts()
    }
}