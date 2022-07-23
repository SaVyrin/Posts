package ru.surf.gallery.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import ru.surf.gallery.data.network.NetworkApi
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    private const val BASE_URL = "https://pictures.chronicker.fun/api/"

    @Provides
    @Singleton
    fun provideNetworkModule(): NetworkApi {
        val contentType = "application/json".toMediaType()
        val converterFactory = Json.asConverterFactory(contentType)
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(converterFactory)
            .build()
            .create(NetworkApi::class.java)
    }

}