package ru.surf.gallery.ui.post

import androidx.lifecycle.ViewModel
import ru.surf.gallery.data.database.PostDao

class PostViewModel(
    postId: String,
    postDao: PostDao
) : ViewModel() {
    val post = postDao.get(postId)
}