package ru.surf.gallery.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.surf.gallery.data.database.PostDao
import ru.surf.gallery.data.database.PostDatabase
import ru.surf.gallery.data.database.UserDao
import ru.surf.gallery.data.database.UserTokenDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): PostDatabase {
        return Room.databaseBuilder(
            appContext.applicationContext,
            PostDatabase::class.java,
            "post_database"
        ).build()
    }

    @Provides
    fun provideUserDao(database: PostDatabase): UserDao {
        return database.userDao
    }

    @Provides
    fun provideUserTokenDao(database: PostDatabase): UserTokenDao {
        return database.userTokenDao
    }

    @Provides
    fun providePostDao(database: PostDatabase): PostDao {
        return database.postDao
    }
}