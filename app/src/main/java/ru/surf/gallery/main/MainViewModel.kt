package ru.surf.gallery.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.surf.gallery.database.*
import ru.surf.gallery.rest.PostApi
import ru.surf.gallery.rest.PostResponse
import java.lang.Exception

class MainViewModel(
    private val userTokenDao: UserTokenDao,
    private val userDao: UserDao,
    private val postDao: PostDao
) : ViewModel() {

    val userTokenFromDao: LiveData<List<UserToken>> = userTokenDao.getAll()
    private var userToken = ""
    val posts = postDao.getAll()

    private val mutablePostsRequestStatus = MutableLiveData<PostsRequestStatus>()
    val postsRequestStatus: LiveData<PostsRequestStatus> = mutablePostsRequestStatus

    fun setUserToken(tokenFromDao: UserToken) {
        userToken = tokenFromDao.token
        getPosts()
    }

    fun getPosts() {
        viewModelScope.launch {
            try {
                mutablePostsRequestStatus.value = PostsRequestStatus.LOADING
                getPostsFromRest()
            } catch (error: Throwable) {
                mutablePostsRequestStatus.value = PostsRequestStatus.ERROR_LOAD
            }
        }
    }

    fun refreshPosts() {
        viewModelScope.launch {
            try {
                mutablePostsRequestStatus.value = PostsRequestStatus.REFRESHING
                getPostsFromRest()
            } catch (error: Throwable) {
                mutablePostsRequestStatus.value = PostsRequestStatus.ERROR_REFRESH
            }
        }
    }

    private suspend fun getPostsFromRest() {
        val postsResponse = sendPostsRequest(userToken)
        when {
            postsResponse.code() == 401 -> {
                clearUserData()
                mutablePostsRequestStatus.value = PostsRequestStatus.UNAUTHORIZED
            }
            postsResponse.body().isNullOrEmpty() -> {
                mutablePostsRequestStatus.value = PostsRequestStatus.ERROR_LOAD
            }
            postsResponse.isSuccessful -> {
                postsResponse.body()?.let {
                    addPostsToDb(it)
                    Log.e("Request", "$postsResponse")
                    mutablePostsRequestStatus.value = PostsRequestStatus.SUCCESS
                    // TODO добавить обработку ошибок
                }
            }
            else -> {
                mutablePostsRequestStatus.value = PostsRequestStatus.ERROR_LOAD
            }
        }
    }

    private suspend fun sendPostsRequest(userToken: String): Response<List<PostResponse>> {
        val postApi = PostApi.create()
        return postApi.getPosts("Token $userToken")
    }

    private suspend fun addPostsToDb(postsReq: List<PostResponse>) {
        val postsToInsert = postsReq.map { it.toPost() }
        postDao.insertAll(postsToInsert) // TODO так не скачет список, когда первый раз заносишь в бд
    }

    fun featuredIconClicked(post: Post) {
        viewModelScope.launch {
            when (post.inFeatured) {
                true -> removeFromFeatured(post)
                false -> addToFeatured(post)
            }
        }
    }

    private suspend fun addToFeatured(post: Post) {
        postDao.update(createUpdatedPost(true, post)) // TODO так сразу обновляется список
    }

    private suspend fun removeFromFeatured(post: Post) {
        postDao.update(createUpdatedPost(false, post))
    }

    private fun createUpdatedPost(inFeatured: Boolean, post: Post): Post {
        val currentTime = System.currentTimeMillis()
        return Post(
            post.id,
            post.title,
            post.content,
            post.photoUrl,
            post.publicationDate,
            inFeatured,
            currentTime
        )
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