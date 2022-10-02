package ru.surf.gallery.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "post_featured",
    primaryKeys = [
        "user_id",
        "post_id"
    ]
)
data class PostInFeatured(
    @ColumnInfo(name = "user_id")
    val userId: String = "",

    @ColumnInfo(name = "post_id")
    val postId: String = "",

    @ColumnInfo(name = "in_featured_date")
    val inFeaturedDate: Long = 0
)