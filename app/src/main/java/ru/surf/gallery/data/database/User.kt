package ru.surf.gallery.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    var id: String = "",

    @ColumnInfo(name = "user_phone")
    var phone: String = "",

    @ColumnInfo(name = "user_email")
    var email: String = "",

    @ColumnInfo(name = "user_first_name")
    var firstName: String = "",

    @ColumnInfo(name = "user_last_name")
    var lastName: String = "",

    @ColumnInfo(name = "user_avatar")
    var avatar: String = "",

    @ColumnInfo(name = "user_city")
    var city: String = "",

    @ColumnInfo(name = "user_about")
    var about: String = ""
)