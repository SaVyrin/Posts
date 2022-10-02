package ru.surf.gallery.domain

import retrofit2.Response
import ru.surf.gallery.data.database.*
import ru.surf.gallery.data.network.NetworkApi
import ru.surf.gallery.data.network.PostResponse
import ru.surf.gallery.ui.main.PostsRequestStatus
import ru.surf.gallery.utils.toPost

class PostsRepositoryNetworkImpl(
    private val networkApi: NetworkApi,
    private val userTokenDao: UserTokenDao,
    private val userDao: UserDao,
    private val postDao: PostDao
) : PostsRepository {
    override suspend fun getPosts(userToken: String): PostsRequestStatus {
        val postsResponse = sendPostsRequest(userToken)
        var postsRequestStatus = PostsRequestStatus.LOADING
        when {
            postsResponse.code() == 401 -> {
                clearUserData()
                postsRequestStatus = PostsRequestStatus.UNAUTHORIZED
            }
            postsResponse.body().isNullOrEmpty() -> {
                postsRequestStatus = PostsRequestStatus.ERROR_LOAD
            }
            postsResponse.isSuccessful -> {
                postsResponse.body()?.let {
                    addPostsToDb(it)
                    postsRequestStatus = PostsRequestStatus.SUCCESS
                }
            }
            else -> {
                postsRequestStatus = PostsRequestStatus.ERROR_LOAD
            }
        }
        return postsRequestStatus
    }

    override suspend fun getFeaturedPosts(userId: String): List<Post> {
        val featuredPostsIds = postDao.getFeaturedPostsIdsAndDates(userId)
        return postDao.getFeaturedPosts(featuredPostsIds.map { it.postId })
    }


    override suspend fun addToFeatured(userId: String, post: Post) {
        postDao.addToFeatured(
            PostInFeatured(userId, post.id)
        )
    }

    override suspend fun removeFromFeatured(userId: String, post: Post) {
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

    override suspend fun clearUserData() {
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
    }
}