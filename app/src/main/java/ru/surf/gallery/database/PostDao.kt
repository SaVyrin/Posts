package ru.surf.gallery.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PostDao {
    @Insert
    suspend fun insert(post: Post)

    @Update
    suspend fun update(post: Post)

    @Delete
    suspend fun delete(post: Post)

    @Query("SELECT * FROM post_table WHERE post_id = :postId")
    fun get(postId: Long): LiveData<Post>

    @Query("SELECT * FROM post_table ORDER BY post_id DESC")
    fun getAll(): LiveData<List<Post>>
}