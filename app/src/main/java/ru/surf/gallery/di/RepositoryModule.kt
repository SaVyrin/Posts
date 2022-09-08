package ru.surf.gallery.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.surf.gallery.data.database.PostDao
import ru.surf.gallery.data.database.UserDao
import ru.surf.gallery.data.database.UserTokenDao
import ru.surf.gallery.data.network.NetworkApi
import ru.surf.gallery.domain.*
import javax.inject.Named


@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Named("network_user")
    fun provideNetworkUserRepository(
        networkApi: NetworkApi,
        userTokenDao: UserTokenDao,
        userDao: UserDao,
        postDao: PostDao
    ): UserRepository {
        return UserRepositoryNetworkImpl(networkApi, userTokenDao, userDao, postDao)
    }

    @Provides
    @Named("network_posts")
    fun provideNetworkPostsRepository(
        networkApi: NetworkApi,
        userTokenDao: UserTokenDao,
        userDao: UserDao,
        postDao: PostDao
    ): PostsRepository {
        return PostsRepositoryNetworkImpl(networkApi, userTokenDao, userDao, postDao)
    }
}