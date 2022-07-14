package ru.surf.gallery.featured

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.surf.gallery.database.Post
import ru.surf.gallery.databinding.FragmentFeaturedBinding

class FeaturedPostRecyclerViewAdapter(val clickListener: (taskId: Long) -> Unit) :
    ListAdapter<Post, FeaturedPostRecyclerViewAdapter.PostItemViewHolder>(PostDiffItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemViewHolder {
        return PostItemViewHolder.inflateFrom(parent)
    }

    override fun onBindViewHolder(holder: PostItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class PostItemViewHolder(val binding: FragmentFeaturedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): PostItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentFeaturedBinding.inflate(layoutInflater, parent, false)
                return PostItemViewHolder(binding)
            }
        }

        fun bind(item: Post, clickListener: (taskId: Long) -> Unit) {
            binding.name.text = item.title
            binding.date.text = item.publicationDate.toString()
            //binding.image.load(item.postImagePath)
            binding.root.setOnClickListener { clickListener(item.id.toLong()) }
        }
    }

    class PostDiffItemCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
            (oldItem.id == newItem.id)

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
            (oldItem == newItem)
    }
}