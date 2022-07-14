package ru.surf.gallery.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.surf.gallery.database.UserDao
import ru.surf.gallery.login.LoginViewModel

class ProfileViewModel(val userDao: UserDao) : ViewModel() {

    val user = userDao.getAll()

    suspend fun logOutUser() {
// TODO logOutUser
    }
}