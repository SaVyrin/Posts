package ru.surf.gallery.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.surf.gallery.R
import ru.surf.gallery.database.Post
import ru.surf.gallery.databinding.FragmentMainListItemBinding

class MainPostRecyclerViewAdapter(
    private val featuredClickListener: (post: Post) -> Unit,
    private val navigateClickListener: (post: Post) -> Unit
) :
    ListAdapter<Post, MainPostRecyclerViewAdapter.PostItemViewHolder>(PostDiffItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemViewHolder {
        return PostItemViewHolder.inflateFrom(parent)
    }

    override fun onBindViewHolder(holder: PostItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, featuredClickListener, navigateClickListener)
    }

    class PostItemViewHolder(private val binding: FragmentMainListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): PostItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentMainListItemBinding.inflate(layoutInflater, parent, false)
                return PostItemViewHolder(binding)
            }
        }

        fun bind(
            item: Post,
            featuredClickListener: (post: Post) -> Unit,
            navigateClickListener: (post: Post) -> Unit
        ) {
            binding.tvPostName.text = item.title
            binding.postImage.load(item.photoUrl)
            when (item.inFeatured) {
                true -> binding.featuredImage.load(R.drawable.ic_heart_fill)
                false -> binding.featuredImage.load(R.drawable.ic_heart_line)
            }
            binding.featuredImage.setOnClickListener { featuredClickListener(item) }
            binding.root.setOnClickListener { navigateClickListener(item) }
        }
    }

    class PostDiffItemCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
            (oldItem.id == newItem.id)

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
            (oldItem == newItem)
    }
}