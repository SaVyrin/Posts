package ru.surf.gallery.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.surf.gallery.database.PostDao


class SearchViewModelFactory(
    private val postDao: PostDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(postDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}