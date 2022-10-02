package ru.surf.gallery.data.database

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

    @Query("SELECT * FROM user_table WHERE user_phone = :login")
    suspend fun getByLogin(login: String): User

    @Query("SELECT * FROM user_table WHERE user_id = :userId")
    suspend fun getById(userId: String): User

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()
}