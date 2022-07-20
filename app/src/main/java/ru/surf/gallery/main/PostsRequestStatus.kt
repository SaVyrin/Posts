package ru.surf.gallery.main

enum class PostsRequestStatus {
    LOADING,
    REFRESHING,
    SUCCESS,
    UNAUTHORIZED,
    ERROR_LOAD,
    ERROR_REFRESH
}