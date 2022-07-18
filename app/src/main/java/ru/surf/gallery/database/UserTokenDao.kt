package ru.surf.gallery.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface UserTokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // TODO убрать onConflict
    suspend fun insert(token: UserToken)

    @Update
    suspend fun update(token: UserToken)

    @Delete
    suspend fun delete(token: UserToken)

    @Query("SELECT * FROM token_table")
    fun get(): LiveData<List<UserToken>>

    @Query("SELECT * FROM token_table")
    fun getAll(): LiveData<List<UserToken>>

    @Query("DELETE FROM token_table")
    fun deleteAll()

    // TODO сделать получение только 1 токена
}