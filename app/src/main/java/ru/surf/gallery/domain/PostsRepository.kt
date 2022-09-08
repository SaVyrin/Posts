package ru.surf.gallery.domain

import ru.surf.gallery.data.database.Post
import ru.surf.gallery.ui.main.PostsRequestStatus

interface PostsRepository {
    suspend fun getPosts(userToken: String): PostsRequestStatus
    suspend fun addToFeatured(post: Post)
    suspend fun removeFromFeatured(post: Post)
}