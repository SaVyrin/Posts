package ru.surf.gallery.login

sealed class LoginStatus {
    val NOT_LOGGED_IN = 0
    val LOGGED_IN = 1
    val ERROR = 2
}