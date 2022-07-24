package ru.surf.gallery.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.surf.gallery.data.database.*
import ru.surf.gallery.data.network.NetworkApi
import ru.surf.gallery.data.network.PostResponse
import ru.surf.gallery.utils.createUpdatedPost
import ru.surf.gallery.utils.toPost
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userTokenDao: UserTokenDao,
    private val userDao: UserDao,
    private val postDao: PostDao,
    private val networkApi: NetworkApi
) : ViewModel() {

    val userTokenFromDao = userTokenDao.get()
    private var userToken = ""

    private val mutablePostsRequestStatus = MutableLiveData<PostsRequestStatus>()
    val postsRequestStatus: LiveData<PostsRequestStatus> = mutablePostsRequestStatus

    val posts = postDao.getAll()

    fun setUserToken(tokenFromDao: UserToken) {
        userToken = tokenFromDao.token
        getPosts()
    }

    fun getPosts() {
        viewModelScope.launch {
            try {
                mutablePostsRequestStatus.value = PostsRequestStatus.LOADING
                getPostsFromServer()
            } catch (error: Throwable) {
                error.printStackTrace()
                mutablePostsRequestStatus.value = PostsRequestStatus.ERROR_LOAD
            }
        }
    }

    fun refreshPosts() {
        viewModelScope.launch {
            try {
                mutablePostsRequestStatus.value = PostsRequestStatus.REFRESHING
                getPostsFromServer()
            } catch (error: Throwable) {
                error.printStackTrace()
                mutablePostsRequestStatus.value = PostsRequestStatus.ERROR_REFRESH
            }
        }
    }

    private suspend fun getPostsFromServer() {
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
                    mutablePostsRequestStatus.value = PostsRequestStatus.SUCCESS
                }
            }
            else -> {
                mutablePostsRequestStatus.value = PostsRequestStatus.ERROR_LOAD
            }
        }
    }

    private suspend fun sendPostsRequest(userToken: String): Response<List<PostResponse>> {
        return networkApi.getPosts("Token $userToken")
    }

    private suspend fun addPostsToDb(postsReq: List<PostResponse>) {
        val postsToInsert = postsReq.map { it.toPost() }
        postDao.insertAll(postsToInsert)
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
        postDao.update(post.createUpdatedPost(true)) // так сразу обновляется список
    }

    private suspend fun removeFromFeatured(post: Post) {
        postDao.update(post.createUpdatedPost(false))
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