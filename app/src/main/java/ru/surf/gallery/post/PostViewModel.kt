package ru.surf.gallery.post

import androidx.lifecycle.ViewModel
import ru.surf.gallery.database.PostDao

class PostViewModel(
    postId: String,
    postDao: PostDao
) : ViewModel() {

    val post = postDao.get(postId)

}