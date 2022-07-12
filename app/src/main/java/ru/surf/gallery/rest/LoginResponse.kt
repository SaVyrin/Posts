package ru.surf.gallery.rest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.surf.gallery.database.User

@Serializable
data class LoginResponse(
    @SerialName("token") val token: String?,
    @SerialName("user_info") val userInfo: User
)