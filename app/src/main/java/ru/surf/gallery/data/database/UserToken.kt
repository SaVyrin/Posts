package ru.surf.gallery.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "token_table")
data class UserToken(
    @PrimaryKey
    @ColumnInfo(name = "user_token")
    var token: String = ""
)