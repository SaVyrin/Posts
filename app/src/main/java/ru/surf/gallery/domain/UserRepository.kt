package ru.surf.gallery.domain

import ru.surf.gallery.ui.login.LoginStatus
import ru.surf.gallery.ui.profile.LogoutStatus

interface UserRepository {
    suspend fun login(login:String, password: String): LoginStatus
    suspend fun logout(token: String): LogoutStatus
    suspend fun clearUserData()
}