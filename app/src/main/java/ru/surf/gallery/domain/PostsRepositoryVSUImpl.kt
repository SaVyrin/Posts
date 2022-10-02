package ru.surf.gallery.domain

import retrofit2.Response
import ru.surf.gallery.data.database.*
import ru.surf.gallery.data.network.NetworkApi
import ru.surf.gallery.data.network.PostResponse
import ru.surf.gallery.utils.toPost

class PostsRepositoryVSUImpl(
    private val networkApi: NetworkApi,
    private val userTokenDao: UserTokenDao,
    private val userDao: UserDao,
    private val postDao: PostDao
) {
    suspend fun getPosts(userId: String): List<Post> {
        val postsIds = postDao.getUserPostsIds(userId)
        val userPosts = postDao.getUserPosts(postsIds)
        val featuredPostsIds = postDao.getFeaturedPostsIdsAndDates(userId)
        userPosts.forEach {
            val post = featuredPostsIds.find { post -> post.postId == it.id }
            it.apply {
                inFeatured = (post != null)
                inFeaturedDate = post?.inFeaturedDate ?: 0
            }
        }
        return userPosts
    }

    suspend fun getFeaturedPosts(userId: String): List<Post> {
        val featuredPostsIds = postDao.getFeaturedPostsIdsAndDates(userId)
        val posts = postDao.getFeaturedPosts(featuredPostsIds.map { it.postId })
        posts.forEach {
            val post = featuredPostsIds.find { post -> post.postId == it.id }
            it.apply {
                inFeatured = (post != null)
                inFeaturedDate = post?.inFeaturedDate ?: 0
            }
        }
        return posts.sortedByDescending { it.inFeaturedDate }
    }


    suspend fun addToFeatured(userId: String, post: Post) {
        postDao.addToFeatured(
            PostInFeatured(userId, post.id, System.currentTimeMillis())
        )
    }

    suspend fun removeFromFeatured(userId: String, post: Post) {
        postDao.deleteFromFeatured(
            PostInFeatured(userId, post.id)
        )
    }

    private suspend fun sendPostsRequest(userToken: String): Response<List<PostResponse>> {
        return networkApi.getPosts("Token $userToken")
    }

    private suspend fun addPostsToDb(postsReq: List<PostResponse>) {
        val postsToInsert = postsReq.map { it.toPost() }
        postDao.insertAll(postsToInsert)
    }

    suspend fun clearUserData() {
        removeUserTokenFromDb()
        removeUserInfoFromDb()
        removePostsFromDb()
    }

    private suspend fun removeUserTokenFromDb() {
        userTokenDao.deleteAll()
    }

    private suspend fun removeUserInfoFromDb() {
        userDao.deleteAll()
    }

    private suspend fun removePostsFromDb() {
        postDao.deleteAllPosts()
        postDao.deleteAllUserPosts()
        postDao.deleteAllFeaturedPosts()
    }
}