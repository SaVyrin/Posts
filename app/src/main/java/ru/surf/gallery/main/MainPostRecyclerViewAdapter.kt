package ru.surf.gallery.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.surf.gallery.database.Post
import ru.surf.gallery.databinding.FragmentMainBinding

class MainPostRecyclerViewAdapter(val clickListener: (taskId: Long) -> Unit) :
    ListAdapter<Post, MainPostRecyclerViewAdapter.PostItemViewHolder>(PostDiffItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemViewHolder {
        return PostItemViewHolder.inflateFrom(parent)
    }

    override fun onBindViewHolder(holder: PostItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class PostItemViewHolder(val binding: FragmentMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): PostItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentMainBinding.inflate(layoutInflater, parent, false)
                return PostItemViewHolder(binding)
            }
        }

        fun bind(item: Post, clickListener: (taskId: Long) -> Unit) {
            binding.tvPostName.text = item.postName
            binding.root.setOnClickListener { clickListener(item.postId) }
        }
    }

    class PostDiffItemCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
            (oldItem.postId == newItem.postId)

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
            (oldItem == newItem)
    }
}