package ru.surf.gallery.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface UserDao {
    @Insert(onConflict = REPLACE) // TODO убрать onConflict
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM user_table")
    fun get(): LiveData<List<User>>

    @Query("SELECT * FROM user_table")
    fun getAll(): LiveData<List<User>>

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

    // TODO сделать получение только 1 юзера
}