package ru.surf.gallery.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.surf.gallery.database.UserDao


class LoginViewModelFactory(private val dao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
