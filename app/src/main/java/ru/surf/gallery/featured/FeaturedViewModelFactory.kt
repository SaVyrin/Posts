package ru.surf.gallery.featured

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.surf.gallery.database.PostDao


class FeaturedViewModelFactory(
    private val postDao: PostDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeaturedViewModel::class.java)) {
            return FeaturedViewModel(postDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}