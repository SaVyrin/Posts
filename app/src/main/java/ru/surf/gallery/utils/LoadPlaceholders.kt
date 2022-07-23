package ru.surf.gallery.utils

import android.content.Context
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

fun getSmallPlaceholder(context: Context): CircularProgressDrawable {
    val placeholder = CircularProgressDrawable(context)
    placeholder.strokeWidth = 5f
    placeholder.centerRadius = 15f
    placeholder.start()
    return placeholder
}

fun getLargePlaceholder(context: Context): CircularProgressDrawable {
    val placeholder = CircularProgressDrawable(context)
    placeholder.strokeWidth = 5f
    placeholder.centerRadius = 40f
    placeholder.start()
    return placeholder
}