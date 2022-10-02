package ru.surf.gallery.domain

import ru.surf.gallery.data.database.Post
import ru.surf.gallery.ui.main.PostsRequestStatus

interface PostsRepository {
    suspend fun getPosts(userToken: String): PostsRequestStatus
    suspend fun getFeaturedPosts(userId: String): List<Post>
    suspend fun addToFeatured(userId: String, post: Post)
    suspend fun removeFromFeatured(userId: String, post: Post)
    suspend fun clearUserData()
}