package ru.surf.gallery.featured

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.surf.gallery.database.Post
import ru.surf.gallery.databinding.FragmentFeaturedBinding

class FeaturedPostRecyclerViewAdapter(
    private val featuredClickListener: (post: Post) -> Unit,
    private val navigateClickListener: (post: Post) -> Unit
) :
    ListAdapter<Post, FeaturedPostRecyclerViewAdapter.PostItemViewHolder>(PostDiffItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemViewHolder {
        return PostItemViewHolder.inflateFrom(parent)
    }

    override fun onBindViewHolder(holder: PostItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, featuredClickListener, navigateClickListener)
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

        fun bind(
            item: Post,
            featuredClickListener: (post: Post) -> Unit,
            navigateClickListener: (post: Post) -> Unit
        ) {
            binding.name.text = item.title
            binding.content.text = item.content
            binding.image.load(item.photoUrl)
            binding.date.text = item.publicationDate
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