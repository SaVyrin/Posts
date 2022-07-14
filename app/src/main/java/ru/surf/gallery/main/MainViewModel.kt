package ru.surf.gallery.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.surf.gallery.database.*
import ru.surf.gallery.login.LoginViewModel
import ru.surf.gallery.rest.PostApi
import ru.surf.gallery.rest.PostResponse

class MainViewModel(
    private val userTokenDao: UserTokenDao,
    private val postDao: PostDao
) : ViewModel() {

    val userToken = userTokenDao.getAll()
    val posts = postDao.getAll()

    suspend fun getPosts(userToken: String) {
        viewModelScope.launch {
            val postApi = PostApi.create()
            val postsReq =
                async {
                    postApi.getPosts("Token $userToken")
                }.await()

            addPostsToDb(postsReq)
            Log.e("Request", "$postsReq")
        }
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
        post.inFeatured = true
        postDao.update(post)
    }

    private suspend fun removeFromFeatured(post: Post) {
        post.inFeatured = false
        postDao.update(post)
    }
}