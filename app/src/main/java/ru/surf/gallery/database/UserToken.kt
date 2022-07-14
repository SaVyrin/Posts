package ru.surf.gallery.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "token_table")
data class UserToken(
    @SerialName("token")
    @PrimaryKey
    @ColumnInfo(name = "user_token")
    var token: String = "",
)