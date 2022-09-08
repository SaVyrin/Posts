package ru.surf.gallery.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.surf.gallery.domain.UserRepository
import ru.surf.gallery.utils.AuthInputValidator
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LoginViewModel @Inject constructor(
    @Named("network_user") private val userRepository: UserRepository
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
                    val newLoginStatus = userRepository.login(login, password)
                    mutableLoginStatus.value = newLoginStatus
                } catch (error: Throwable) {
                    error.printStackTrace()
                    mutableLoginStatus.value = LoginStatus.ERROR_INTERNET
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        val loginValidity = AuthInputValidator.getLoginValidity(login)
        val passwordValidity = AuthInputValidator.getPasswordValidity(password)
        mutableLoginFieldStatus.value = loginValidity
        mutablePasswordFieldStatus.value = passwordValidity

        return AuthInputValidator.fieldsValid(loginValidity, passwordValidity)
    }
}