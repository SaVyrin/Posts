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
            postDao.update(createUpdatedPost(false, post))
        }
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
}