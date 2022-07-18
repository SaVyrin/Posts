package ru.surf.gallery.featured

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.surf.gallery.database.Post
import ru.surf.gallery.database.PostDao

class FeaturedViewModel(
    private val postDao: PostDao
) : ViewModel() {

    val featuredPosts = postDao.getFeaturedPosts()

    fun removePostFromFeatured(post: Post) {
        viewModelScope.launch {
            post.inFeatured = false
            postDao.update(post)
        }
    }
}