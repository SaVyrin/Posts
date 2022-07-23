package ru.surf.gallery.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("id")
    var id: String = "",

    @SerialName("phone")
    var phone: String = "",

    @SerialName("email")
    var email: String = "",

    @SerialName("firstName")
    var firstName: String = "",

    @SerialName("lastName")
    var lastName: String = "",

    @SerialName("avatar")
    var avatar: String = "",

    @SerialName("city")
    var city: String = "",

    @SerialName("about")
    var about: String = ""
)