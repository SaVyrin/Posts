package ru.surf.gallery.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "user_table")
data class User(
    @SerialName("id")
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    var id: String = "",

    @SerialName("phone")
    @ColumnInfo(name = "user_phone")
    var phone: String = "",

    @SerialName("email")
    @ColumnInfo(name = "user_email")
    var email: String = "",

    @SerialName("firstName")
    @ColumnInfo(name = "user_first_name")
    var firstName: String = "",

    @SerialName("lastName")
    @ColumnInfo(name = "user_last_name")
    var lastName: String = "",

    @SerialName("avatar")
    @ColumnInfo(name = "user_avatar")
    var avatar: String = "",

    @SerialName("city")
    @ColumnInfo(name = "user_city")
    var city: String = "",

    @SerialName("about")
    @ColumnInfo(name = "user_about")
    var about: String = ""
)