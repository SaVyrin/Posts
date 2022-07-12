package ru.surf.gallery.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post_table")
data class Post(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "post_id")
    var postId: Long = 0,

    @ColumnInfo(name = "post_name")
    var postName: String = "",

    @ColumnInfo(name = "post_date")
    var postDate: String = "",

    @ColumnInfo(name = "post_image")
    var postImagePath: String = ""
)