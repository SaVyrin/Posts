package ru.surf.gallery.rest

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PostApi {

    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @GET("cat?json=true")
    suspend fun getPosts(@Query("tag") tag: String?): LoginResponse

    companion object {
        private const val BASE_URL = "https://pictures.chronicker.fun/api/"

        fun create(): PostApi {
            val contentType = MediaType.get("application/json")
            val converterFactory = Json.asConverterFactory(contentType)
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(converterFactory)
                .build()
                .create(PostApi::class.java)
        }
    }
}