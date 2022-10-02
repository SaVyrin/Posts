package ru.surf.gallery.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface PostDao {
    @Insert(onConflict = IGNORE)
    suspend fun insert(post: Post)

    @Insert(onConflict = IGNORE)
    suspend fun insertAll(posts: List<Post>)

    @Insert(onConflict = REPLACE)
    suspend fun addToUserPosts(userPost: UserPost)

    @Insert(onConflict = REPLACE)
    suspend fun addToFeatured(postInFeatured: PostInFeatured)

    @Update
    suspend fun update(post: Post)

    @Delete
    suspend fun delete(post: Post)

    @Delete
    suspend fun deleteFromFeatured(postInFeatured: PostInFeatured)

    @Query("SELECT * FROM post_table WHERE post_id = :postId")
    fun get(postId: String): LiveData<Post>

    @Query("SELECT * FROM post_table")
    fun getAll(): LiveData<List<Post>>

    @Query("SELECT post_id FROM user_post WHERE user_id = :userId")
    suspend fun getUserPostsIds(userId: String): List<String>

    @Query("SELECT * FROM post_table WHERE post_id IN (:postsIds)")
    suspend fun getUserPosts(postsIds: List<String>): List<Post>

    @Query("SELECT * FROM post_featured WHERE user_id = :userId")
    suspend fun getFeaturedPostsIdsAndDates(userId: String): List<PostInFeatured>

    @Query("SELECT * FROM post_table WHERE post_id IN (:postsIds)")
    suspend fun getFeaturedPosts(postsIds: List<String>): List<Post>

    /*@Query("SELECT * FROM post_featured INNER JOIN post_table " +
            "ON post_table.post_id = post_featured.post_id " +
            "WHERE user_id = :userId")
    fun getFeaturedPosts(userId: Long): LiveData<List<Post>>*/

    @Query("DELETE FROM post_table")
    suspend fun deleteAllPosts()

    @Query("DELETE FROM user_post")
    suspend fun deleteAllUserPosts()

    @Query("DELETE FROM post_featured")
    suspend fun deleteAllFeaturedPosts()
}