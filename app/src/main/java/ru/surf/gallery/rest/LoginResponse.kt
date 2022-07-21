package ru.surf.gallery.rest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("token") val token: String,
    @SerialName("user_info") val userInfo: UserResponse
)