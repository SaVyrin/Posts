package ru.surf.gallery.rest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LoginRequest(
    @SerialName("phone") val phone: String,
    @SerialName("password") val password: String
)
