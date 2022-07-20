package ru.surf.gallery.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.surf.gallery.database.*
import ru.surf.gallery.rest.PostApi
import ru.surf.gallery.rest.PostResponse

class MainViewModel(
    private val userTokenDao: UserTokenDao,
    private val postDao: PostDao
) : ViewModel() {

    val userToken = userTokenDao.getAll()
    val posts = postDao.getAll()

    private val mutablePostsRequestStatus = MutableLiveData<PostsRequestStatus>()
    val postsRequestStatus: LiveData<PostsRequestStatus> = mutablePostsRequestStatus

    suspend fun getPosts(userToken: String) {
        viewModelScope.launch {
            try {
                mutablePostsRequestStatus.value = PostsRequestStatus.IN_PROGRESS
                val postsResponse = sendPostsRequest(userToken)
                addPostsToDb(postsResponse)
                Log.e("Request", "$postsResponse")
                mutablePostsRequestStatus.value = PostsRequestStatus.SUCCESS
                // TODO добавить обработку ошибок
            } catch (error: Throwable) {
                mutablePostsRequestStatus.value = PostsRequestStatus.ERROR_LOAD
            }
        }
    }

    private suspend fun sendPostsRequest(userToken: String): List<PostResponse> {
        val postApi = PostApi.create()
        return postApi.getPosts("Token $userToken")
    }

    private suspend fun addPostsToDb(postsReq: List<PostResponse>) {
        for (postResponse in postsReq) {
            addPostToDb(postResponse.toPost())
        }
    }

    private suspend fun addPostToDb(post: Post) {
        postDao.insert(post)
    }

    suspend fun featuredIconClicked(post: Post) {
        when (post.inFeatured) {
            true -> removeFromFeatured(post)
            false -> addToFeatured(post)
        }
    }

    private suspend fun addToFeatured(post: Post) {
        postDao.update(createUpdatedPost(true, post)) // TODO так сразу обновляется список
    }

    private suspend fun removeFromFeatured(post: Post) {
        postDao.update(createUpdatedPost(false, post))
    }

    private fun createUpdatedPost(inFeatured: Boolean, post: Post): Post {
        return Post(
            post.id,
            post.title,
            post.content,
            post.photoUrl,
            post.publicationDate,
            inFeatured
        )
    }
}