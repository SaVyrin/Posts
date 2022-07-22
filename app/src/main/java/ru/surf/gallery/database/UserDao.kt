package ru.surf.gallery.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface UserDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM user_table LIMIT 1")
    fun get(): LiveData<User>

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()
}