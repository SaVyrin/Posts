package ru.surf.gallery.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.surf.gallery.database.User
import ru.surf.gallery.database.UserDao
import ru.surf.gallery.database.UserToken
import ru.surf.gallery.database.UserTokenDao
import ru.surf.gallery.rest.LoginRequest
import ru.surf.gallery.rest.LoginResponse
import ru.surf.gallery.rest.PostApi

class LoginViewModel(
    val userTokenDao: UserTokenDao,
    val userDao: UserDao
) : ViewModel() {

    private val mutableLoginStatus = MutableLiveData(LoginStatus.NOT_LOGGED_IN)
    val loginStatus: LiveData<LoginStatus> = mutableLoginStatus

    private val mutableLoginFieldStatus = MutableLiveData<LoginFieldStatus>()
    val loginFieldStatus: LiveData<LoginFieldStatus> = mutableLoginFieldStatus

    private val mutablePasswordFieldStatus = MutableLiveData<PasswordFieldStatus>()
    val passwordFieldStatus: LiveData<PasswordFieldStatus> = mutablePasswordFieldStatus

    private var login = ""
    private var password = ""

    fun setLogin(newLoginValue: String) {
        login = newLoginValue
    }

    fun setPassword(newPasswordValue: String) {
        password = newPasswordValue
    }

    suspend fun logInUser() {
        if (validateInputs()) {
            viewModelScope.launch {
                try {
                    mutableLoginStatus.value = LoginStatus.IN_PROGRESS
                    val loginResponse = sendLoginRequest()
                    addTokenToDb(loginResponse.token)
                    addUserToDb(loginResponse.userInfo)
                    Log.e("Request", loginResponse.token)
                    mutableLoginStatus.value = LoginStatus.LOGGED_IN
                    // TODO добавить обработку ошибок
                } catch (error: Throwable) {
                    mutableLoginStatus.value = LoginStatus.ERROR
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        val loginValidity = InputValidator.getLoginValidity(login)
        val passwordValidity = InputValidator.getPasswordValidity(password)
        mutableLoginFieldStatus.value = loginValidity
        mutablePasswordFieldStatus.value = passwordValidity

        return InputValidator.fieldsValid(loginValidity, passwordValidity)
    }

    private suspend fun sendLoginRequest(): LoginResponse {
        val postApi = PostApi.create()
        return postApi.login(LoginRequest("+7$login", password))
    }

    private suspend fun addTokenToDb(tokenString: String) {
        val userToken = UserToken(tokenString)
        userTokenDao.insert(userToken)
        Log.e("Request", "SUCCESS") // TODO убрать заменить или оставить логирование
    }

    private suspend fun addUserToDb(user: User) {
        userDao.insert(user)
        Log.e("Request", "SUCCESS")
    }
}