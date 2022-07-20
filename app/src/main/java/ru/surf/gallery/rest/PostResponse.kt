package ru.surf.gallery.rest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.surf.gallery.database.Post
import java.text.SimpleDateFormat
import java.util.*

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
) {
    fun toPost(): Post {
        return Post(
            id,
            title,
            content,
            photoUrl,
            convertLongToTime(publicationDate)
        )
    }
    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val dateFormatter = SimpleDateFormat("dd.MM.yyyy")
        return dateFormatter.format(date)
    }
}