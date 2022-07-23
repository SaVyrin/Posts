package ru.surf.gallery.ui.main

enum class PostsRequestStatus {
    LOADING,
    REFRESHING,
    SUCCESS,
    UNAUTHORIZED,
    ERROR_LOAD,
    ERROR_REFRESH
}