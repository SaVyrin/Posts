package ru.surf.gallery.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "user_post",
    primaryKeys = [
        "user_id",
        "post_id"
    ]
)
data class UserPost(
    @ColumnInfo(name = "user_id")
    val userId: String = "",

    @ColumnInfo(name = "post_id")
    val postId: String = "",
)