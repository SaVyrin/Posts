package ru.surf.gallery.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM user_table")
    fun get(): LiveData<List<User>>

    @Query("SELECT * FROM user_table")
    fun getAll(): LiveData<List<User>>
}