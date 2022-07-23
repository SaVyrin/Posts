package ru.surf.gallery.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PostResponse(
    @SerialName("id")
    val id: String,

    @SerialName("title")
    val title: String,

    @SerialName("content")
    val content: String,

    @SerialName("photoUrl")
    val photoUrl: String,

    @SerialName("publicationDate")
    val publicationDate: Long,
)