package ru.surf.gallery.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.surf.gallery.database.PostDao


class PostViewModelFactory(
    private val postId: String,
    private val postDao: PostDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            return PostViewModel(postId, postDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}