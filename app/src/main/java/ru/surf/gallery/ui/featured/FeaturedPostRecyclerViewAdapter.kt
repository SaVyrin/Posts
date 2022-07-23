package ru.surf.gallery.ui.featured

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.surf.gallery.data.database.Post
import ru.surf.gallery.databinding.FragmentFeaturedListItemBinding
import ru.surf.gallery.utils.PostDiffItemCallback
import ru.surf.gallery.utils.getLargePlaceholder

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

    class PostItemViewHolder(private val binding: FragmentFeaturedListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): PostItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentFeaturedListItemBinding.inflate(layoutInflater, parent, false)
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
            setPostContent(item)
            setPostPublicationDate(item)
            setFeaturedClickListener(item, featuredClickListener)
            setNavigateClickListener(item, navigateClickListener)
        }

        private fun setPostTitle(item: Post) {
            binding.postTitleTv.text = item.title
        }

        private fun setPostImage(item: Post) {
            val placeholder = getLargePlaceholder(binding.root.context)
            binding.postImage.load(item.photoUrl) {
                crossfade(true)
                placeholder(placeholder)
            }
        }

        private fun setPostContent(item: Post) {
            binding.postContentTv.text = item.content
        }

        private fun setPostPublicationDate(item: Post) {
            binding.postPublicationDateTv.text = item.publicationDate
        }

        private fun setFeaturedClickListener(
            item: Post,
            featuredClickListener: (post: Post) -> Unit
        ) {
            binding.featuredHeartImage.setOnClickListener { featuredClickListener(item) }
        }

        private fun setNavigateClickListener(
            item: Post,
            navigateClickListener: (post: Post) -> Unit
        ) {
            binding.root.setOnClickListener { navigateClickListener(item) }
        }
    }
}