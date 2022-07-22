package ru.surf.gallery.featured

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

    val featuredPosts = postDao.getFeaturedPosts()

    fun removePostFromFeatured(post: Post) {
        viewModelScope.launch {
            postDao.update(post.createUpdatedPost(false))
        }
    }
}