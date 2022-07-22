package ru.surf.gallery.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.surf.gallery.database.UserTokenDao


class SplashViewModelFactory(
    private val userTokenDao: UserTokenDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(userTokenDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}