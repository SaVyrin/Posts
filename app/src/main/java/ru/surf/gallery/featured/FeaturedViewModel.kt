package ru.surf.gallery.featured

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.surf.gallery.database.Post
import ru.surf.gallery.database.PostDao
import ru.surf.gallery.utils.createUpdatedPost
import javax.inject.Inject

@HiltViewModel
class FeaturedViewModel @Inject constructor(
    private val postDao: PostDao
) : ViewModel() {

    private val mutableFeaturedScreenState = MutableLiveData(FeaturedState.NO_POSTS)
    val featuredScreenState: LiveData<FeaturedState> = mutableFeaturedScreenState

    val featuredPosts = postDao.getFeaturedPosts()

    fun setFeaturedScreenState(currentPosts: List<Post>) {
        when (currentPosts.isEmpty()) {
            true -> mutableFeaturedScreenState.value = FeaturedState.NO_POSTS
            false -> mutableFeaturedScreenState.value = FeaturedState.SHOW_POSTS
        }
    }

    fun removePostFromFeatured(post: Post) {
        viewModelScope.launch {
            postDao.update(post.createUpdatedPost(false))
        }
    }
}