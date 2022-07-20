package ru.surf.gallery.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE

@Dao
interface PostDao {
    @Insert(onConflict = IGNORE) // TODO убрать onConflict
    suspend fun insert(post: Post)

    @Insert(onConflict = IGNORE)
    suspend fun insertAll(posts: List<Post>)

    @Update
    suspend fun update(post: Post)

    @Delete
    suspend fun delete(post: Post)

    @Query("SELECT * FROM post_table WHERE post_id = :postId")
    fun get(postId: String): LiveData<Post>

    @Query("SELECT * FROM post_table ORDER BY post_id DESC")
    fun getAll(): LiveData<List<Post>>

    @Query("SELECT * FROM post_table WHERE post_in_featured > 0")
    fun getFeaturedPosts(): LiveData<List<Post>>

    @Query("DELETE FROM post_table")
    suspend fun deleteAll()

    // TODO сделать получение только 1 поста
}