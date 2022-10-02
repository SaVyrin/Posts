package ru.surf.gallery.ui.featured

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.surf.gallery.data.database.*
import ru.surf.gallery.domain.PostsRepository
import ru.surf.gallery.domain.PostsRepositoryVSUImpl
import ru.surf.gallery.utils.createUpdatedPost
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class FeaturedViewModel @Inject constructor(
    private val postsRepository: PostsRepositoryVSUImpl,
    private val postDao: PostDao,
    private val userTokenDao: UserTokenDao
) : ViewModel() {

    private val mutableFeaturedScreenState = MutableLiveData(FeaturedState.NO_POSTS)
    val featuredScreenState: LiveData<FeaturedState> = mutableFeaturedScreenState

    val user = userTokenDao.get()
    var userId = ""
    var featuredPosts = MutableLiveData<List<Post>>()


    fun setFeaturedScreenState(currentPosts: List<Post>) {
        when (currentPosts.isEmpty()) {
            true -> mutableFeaturedScreenState.value = FeaturedState.NO_POSTS
            false -> mutableFeaturedScreenState.value = FeaturedState.SHOW_POSTS
        }
    }

    fun getPosts(userId: String) {
        viewModelScope.launch {
            featuredPosts.value = postsRepository.getFeaturedPosts(userId)
        }
    }

    fun removePostFromFeatured(post: Post) {
        viewModelScope.launch {
            postsRepository.removeFromFeatured(userId, post)
            featuredPosts.value = postsRepository.getFeaturedPosts(userId)
        }
    }
}