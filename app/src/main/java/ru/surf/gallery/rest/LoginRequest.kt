package ru.surf.gallery.rest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("phone") val phone: String,
    @SerialName("password") val password: String
)
