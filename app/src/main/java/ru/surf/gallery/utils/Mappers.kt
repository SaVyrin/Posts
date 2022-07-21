package ru.surf.gallery.utils

import kotlinx.serialization.SerialName
import ru.surf.gallery.database.Post
import ru.surf.gallery.database.User
import ru.surf.gallery.rest.PostResponse
import ru.surf.gallery.rest.UserResponse
import java.text.SimpleDateFormat
import java.util.*


fun UserResponse.toUser(): User {
    return User(
        id,
        phone,
        email,
        firstName,
        lastName,
        avatar,
        city,
        about
    )
}

fun PostResponse.toPost(): Post {
    return Post(
        id,
        title,
        content,
        photoUrl,
        convertLongToTime(publicationDate)
    )
}

fun Post.createUpdatedPost(inFeatured: Boolean): Post {
    val currentTime = System.currentTimeMillis()
    return Post(
        id,
        title,
        content,
        photoUrl,
        publicationDate,
        inFeatured,
        currentTime
    )
}

private fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val dateFormatter = SimpleDateFormat("dd.MM.yyyy")
    return dateFormatter.format(date)
}