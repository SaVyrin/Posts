package ru.surf.gallery.domain

import retrofit2.Response
import ru.surf.gallery.data.database.Post
import ru.surf.gallery.data.database.PostDao
import ru.surf.gallery.data.database.UserDao
import ru.surf.gallery.data.database.UserTokenDao
import ru.surf.gallery.data.network.NetworkApi
import ru.surf.gallery.data.network.PostResponse
import ru.surf.gallery.ui.main.PostsRequestStatus
import ru.surf.gallery.utils.createUpdatedPost
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


    override suspend fun addToFeatured(post: Post) {
        postDao.update(post.createUpdatedPost(true)) // так сразу обновляется список
    }

    override suspend fun removeFromFeatured(post: Post) {
        postDao.update(post.createUpdatedPost(false))
    }

    private suspend fun sendPostsRequest(userToken: String): Response<List<PostResponse>> {
        return networkApi.getPosts("Token $userToken")
    }

    private suspend fun addPostsToDb(postsReq: List<PostResponse>) {
        val postsToInsert = postsReq.map { it.toPost() }
        postDao.insertAll(postsToInsert)
    }

    private suspend fun clearUserData() {
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
        postDao.deleteAll()
    }
}