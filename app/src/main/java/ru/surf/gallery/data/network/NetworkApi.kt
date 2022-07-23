package ru.surf.gallery.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface NetworkApi {

    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(
        @Header(AUTHORIZATION_HEADER) loginToken: String
    ): Response<LogoutResponse>

    @GET("picture")
    suspend fun getPosts(
        @Header(AUTHORIZATION_HEADER) loginToken: String
    ): Response<List<PostResponse>>


    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
    }
}