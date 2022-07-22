package ru.surf.gallery.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.surf.gallery.database.PostDao
import ru.surf.gallery.database.UserDao
import ru.surf.gallery.database.UserTokenDao


class MainViewModelFactory(
    private val userTokenDao: UserTokenDao,
    private val userDao: UserDao,
    private val postDao: PostDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(userTokenDao, userDao, postDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}