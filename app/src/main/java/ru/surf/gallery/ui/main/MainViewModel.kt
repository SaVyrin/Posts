package ru.surf.gallery.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.surf.gallery.data.database.*
import ru.surf.gallery.domain.PostsRepository
import ru.surf.gallery.domain.PostsRepositoryVSUImpl
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    userTokenDao: UserTokenDao,
    private val postsRepository: PostsRepositoryVSUImpl
) : ViewModel() {

    val user = userTokenDao.get()
    private var userId = ""

    private val mutablePostsRequestStatus = MutableLiveData<PostsRequestStatus>()
    val postsRequestStatus: LiveData<PostsRequestStatus> = mutablePostsRequestStatus

    val posts = MutableLiveData<List<Post>>()

    fun setUserId(userFromDao: UserToken) {
        userId = userFromDao.token
        getPosts()
    }

    fun getPosts() {
        viewModelScope.launch {
            try {
                mutablePostsRequestStatus.value = PostsRequestStatus.LOADING
                posts.value = postsRepository.getPosts(userId)
                mutablePostsRequestStatus.value = PostsRequestStatus.SUCCESS
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
                posts.value = postsRepository.getPosts(userId)
                mutablePostsRequestStatus.value = PostsRequestStatus.SUCCESS
            } catch (error: Throwable) {
                error.printStackTrace()
                mutablePostsRequestStatus.value = PostsRequestStatus.ERROR_REFRESH
            }
        }
    }

    fun featuredIconClicked(post: Post) {
        viewModelScope.launch {
            when (post.inFeatured) {
                true -> postsRepository.removeFromFeatured(userId, post)
                false -> postsRepository.addToFeatured(userId, post)
            }
            posts.value = postsRepository.getPosts(userId)
        }
    }
}