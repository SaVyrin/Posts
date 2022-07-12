package ru.surf.gallery.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.surf.gallery.database.UserDao
import ru.surf.gallery.login.LoginViewModel

class ProfileViewModel(val userDao: UserDao) : ViewModel() {

    private val _loginStatus = MutableLiveData(LoginViewModel.NOT_LOGGED_IN)
    val loginStatus: LiveData<Int> = _loginStatus
    var user = userDao.getAll()


    suspend fun logOutUser() {

    }
}