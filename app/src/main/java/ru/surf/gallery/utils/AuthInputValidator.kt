package ru.surf.gallery.utils

import ru.surf.gallery.ui.login.LoginFieldStatus
import ru.surf.gallery.ui.login.PasswordFieldStatus

abstract class AuthInputValidator {
    companion object {
        fun getLoginValidity(login: String): LoginFieldStatus {
            return when {
                login.isEmpty() -> LoginFieldStatus.EMPTY
                (login.length != 10) -> LoginFieldStatus.NOT_VALID
                else -> LoginFieldStatus.VALID
            }
        }

        fun getPasswordValidity(password: String): PasswordFieldStatus {
            return when {
                password.isEmpty() -> PasswordFieldStatus.EMPTY
                (password.length !in 6..255) -> PasswordFieldStatus.NOT_VALID
                else -> PasswordFieldStatus.VALID
            }
        }

        fun fieldsValid(
            loginFieldStatus: LoginFieldStatus,
            passwordFieldStatus: PasswordFieldStatus
        ): Boolean {
            return ((loginFieldStatus == LoginFieldStatus.VALID) &&
                    (passwordFieldStatus == PasswordFieldStatus.VALID))
        }
    }
}