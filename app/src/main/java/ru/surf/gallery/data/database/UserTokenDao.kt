package ru.surf.gallery.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface UserTokenDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(token: UserToken)

    @Update
    suspend fun update(token: UserToken)

    @Delete
    suspend fun delete(token: UserToken)

    @Query("SELECT * FROM token_table LIMIT 1")
    fun get(): LiveData<UserToken>

    @Query("DELETE FROM token_table")
    suspend fun deleteAll()
}