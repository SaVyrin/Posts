package ru.surf.gallery.featured

import androidx.lifecycle.ViewModel
import ru.surf.gallery.database.PostDao

class FeaturedViewModel(
    private val postDao: PostDao
) : ViewModel() {

    val featuredPosts = postDao.getFeaturedPosts()


}