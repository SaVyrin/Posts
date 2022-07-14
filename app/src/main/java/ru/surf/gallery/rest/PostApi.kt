package ru.surf.gallery.rest

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.*

interface PostApi {

    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @GET("picture")
    suspend fun getPosts(
        @Header(AUTHORIZATION_HEADER) loginToken: String
    ): List<PostResponse>

    companion object {
        private const val BASE_URL = "https://pictures.chronicker.fun/api/"
        private const val AUTHORIZATION_HEADER = "Authorization"

        fun create(): PostApi {
            val contentType = "application/json".toMediaType()
            val converterFactory = Json.asConverterFactory(contentType)
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(converterFactory)
                .build()
                .create(PostApi::class.java)
        }
    }
}