package ru.surf.gallery.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post_table")
data class Post(
    @PrimaryKey
    @ColumnInfo(name = "post_id")
    val id: String = "",

    @ColumnInfo(name = "post_title")
    val title: String = "",

    @ColumnInfo(name = "post_content")
    val content: String ="",

    @ColumnInfo(name = "photo_url")
    val photoUrl: String = "",

    @ColumnInfo(name = "publication_date")
    val publicationDate: String = "",

    @ColumnInfo(name = "post_in_featured")
    var inFeatured: Boolean = false,

    @ColumnInfo(name = "in_featured_date")
    var inFeaturedDate: Long = 0
)