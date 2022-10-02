package ru.surf.gallery.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "post_table")
data class Post(
    @PrimaryKey
    @ColumnInfo(name = "post_id")
    var id: String = "",

    @ColumnInfo(name = "post_title")
    var title: String = "",

    @ColumnInfo(name = "post_content")
    var content: String ="",

    @ColumnInfo(name = "photo_url")
    var photoUrl: String = "",

    @ColumnInfo(name = "publication_date")
    var publicationDate: String = "",

    @Ignore
    var inFeatured: Boolean = false,

    @Ignore
    var inFeaturedDate: Long = 0
)