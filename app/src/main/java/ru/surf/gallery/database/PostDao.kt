package ru.surf.gallery.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface PostDao {
    @Insert(onConflict = REPLACE) // TODO убрать onConflict
    suspend fun insert(post: Post)

    @Update
    suspend fun update(post: Post)

    @Delete
    suspend fun delete(post: Post)

    @Query("SELECT * FROM post_table WHERE post_id = :postId")
    fun get(postId: Long): LiveData<Post>

    @Query("SELECT * FROM post_table ORDER BY post_id DESC")
    fun getAll(): LiveData<List<Post>>

    @Query("SELECT * FROM post_table WHERE post_in_featured > 0")
    fun getFeaturedPosts(): LiveData<List<Post>>

    // TODO сделать получение только 1 поста
}