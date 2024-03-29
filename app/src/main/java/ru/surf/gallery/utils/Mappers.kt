package ru.surf.gallery.utils

import ru.surf.gallery.data.database.Post
import ru.surf.gallery.data.database.User
import ru.surf.gallery.data.database.UserToken
import ru.surf.gallery.data.network.PostResponse
import ru.surf.gallery.data.network.UserResponse
import java.text.SimpleDateFormat
import java.util.*

fun String.toUserToken(): UserToken {
    return UserToken(
        this
    )
}

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