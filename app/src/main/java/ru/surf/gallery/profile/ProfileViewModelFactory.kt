package ru.surf.gallery.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.surf.gallery.database.PostDao
import ru.surf.gallery.database.UserDao
import ru.surf.gallery.database.UserTokenDao


class ProfileViewModelFactory(
    private val userTokenDao: UserTokenDao,
    private val userDao: UserDao,
    private val postDao: PostDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userTokenDao, userDao, postDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
