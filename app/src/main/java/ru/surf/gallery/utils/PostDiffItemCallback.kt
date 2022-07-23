package ru.surf.gallery.utils

import androidx.recyclerview.widget.DiffUtil
import ru.surf.gallery.data.database.Post

class PostDiffItemCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
        (oldItem.id == newItem.id)

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
        (oldItem == newItem)
}