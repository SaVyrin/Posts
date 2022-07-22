package ru.surf.gallery.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.surf.gallery.database.User
import ru.surf.gallery.database.UserDao
import ru.surf.gallery.database.UserToken
import ru.surf.gallery.database.UserTokenDao
import ru.surf.gallery.rest.LoginRequest
import ru.surf.gallery.rest.LoginResponse
import ru.surf.gallery.rest.NetworkApi
import ru.surf.gallery.utils.toUser
import ru.surf.gallery.utils.toUserToken
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userTokenDao: UserTokenDao,
    private val userDao: UserDao,
    private val networkApi: NetworkApi
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

    fun logInUser() {
        if (validateInputs()) {
            viewModelScope.launch {
                try {
                    mutableLoginStatus.value = LoginStatus.IN_PROGRESS

                    val loginResponse = sendLoginRequest()
                    when {
                        loginResponse.code() == 400 -> {
                            mutableLoginStatus.value = LoginStatus.ERROR_WRONG_DATA
                        }
                        loginResponse.isSuccessful -> {
                            loginResponse.body()?.let {
                                addTokenToDb(it.token.toUserToken())
                                addUserToDb(it.userInfo.toUser())
                                mutableLoginStatus.value = LoginStatus.LOGGED_IN
                            }
                        }
                        else -> {
                            mutableLoginStatus.value = LoginStatus.ERROR_INTERNET
                        }
                    }
                } catch (error: Throwable) {
                    mutableLoginStatus.value = LoginStatus.ERROR_INTERNET
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

    private suspend fun sendLoginRequest(): Response<LoginResponse> {
        return networkApi.login(LoginRequest("+7$login", password))
    }

    private suspend fun addTokenToDb(userToken: UserToken) {
        userTokenDao.insert(userToken)
    }

    private suspend fun addUserToDb(user: User) {
        userDao.insert(user)
    }
}