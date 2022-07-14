package ru.surf.gallery.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.surf.gallery.database.UserDao
import ru.surf.gallery.database.UserTokenDao


class LoginViewModelFactory(
    private val userTokenDao: UserTokenDao,
    private val userDao: UserDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userTokenDao, userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
