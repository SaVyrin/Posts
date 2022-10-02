package ru.surf.gallery.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Post::class,
        User::class,
        UserToken::class,
        PostInFeatured::class,
        UserPost::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PostDatabase : RoomDatabase() {
    abstract val postDao: PostDao
    abstract val userDao: UserDao
    abstract val userTokenDao: UserTokenDao
}
