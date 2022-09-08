package ru.surf.gallery.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.surf.gallery.data.database.Post
import ru.surf.gallery.data.database.PostDao
import ru.surf.gallery.data.database.UserToken
import ru.surf.gallery.data.database.UserTokenDao
import ru.surf.gallery.domain.PostsRepository
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    userTokenDao: UserTokenDao,
    postDao: PostDao,
    @Named("network_posts") private val postsRepository: PostsRepository
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
                val newPostsRequestStatus = postsRepository.getPosts(userToken)
                mutablePostsRequestStatus.value = newPostsRequestStatus
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
                val newPostsRequestStatus = postsRepository.getPosts(userToken)
                mutablePostsRequestStatus.value = newPostsRequestStatus
            } catch (error: Throwable) {
                error.printStackTrace()
                mutablePostsRequestStatus.value = PostsRequestStatus.ERROR_REFRESH
            }
        }
    }

    fun featuredIconClicked(post: Post) {
        viewModelScope.launch {
            when (post.inFeatured) {
                true -> postsRepository.removeFromFeatured(post)
                false -> postsRepository.addToFeatured(post)
            }
        }
    }
}