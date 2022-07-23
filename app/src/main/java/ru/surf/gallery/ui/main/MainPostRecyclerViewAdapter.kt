package ru.surf.gallery.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.surf.gallery.R
import ru.surf.gallery.data.database.Post
import ru.surf.gallery.databinding.FragmentMainListItemBinding
import ru.surf.gallery.utils.PostDiffItemCallback
import ru.surf.gallery.utils.getSmallPlaceholder

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
            setPostTitle(item)
            setPostImage(item)
            setFeaturedImage(item)
            setFeaturedClickListener(item, featuredClickListener)
            setNavigateClickListener(item, navigateClickListener)
        }

        private fun setPostTitle(item: Post) {
            binding.postTitleTv.text = item.title
        }

        private fun setPostImage(item: Post) {
            val placeholder = getSmallPlaceholder(binding.root.context)
            binding.mainPostImage.load(item.photoUrl) {
                crossfade(true)
                placeholder(placeholder)
            }
        }

        private fun setFeaturedImage(item: Post) {
            val featuredImageId = getCurrentFeaturedImageId(item)
            binding.mainHeartImage.load(featuredImageId)
        }

        private fun getCurrentFeaturedImageId(item: Post): Int {
            return when (item.inFeatured) {
                true -> R.drawable.ic_heart_fill
                false -> R.drawable.ic_heart_line
            }
        }

        private fun setFeaturedClickListener(
            item: Post,
            featuredClickListener: (post: Post) -> Unit
        ) {
            binding.mainHeartImage.setOnClickListener { featuredClickListener(item) }
        }

        private fun setNavigateClickListener(
            item: Post,
            navigateClickListener: (post: Post) -> Unit
        ) {
            binding.root.setOnClickListener { navigateClickListener(item) }
        }
    }
}