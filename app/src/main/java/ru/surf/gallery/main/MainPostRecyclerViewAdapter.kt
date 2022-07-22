package ru.surf.gallery.main

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import com.bumptech.glide.Glide
import ru.surf.gallery.R
import ru.surf.gallery.database.Post
import ru.surf.gallery.databinding.FragmentMainListItemBinding
import ru.surf.gallery.utils.PostDiffItemCallback

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
            val draw = CircularProgressDrawable(binding.root.context) // TODO перенести в di
            draw.strokeWidth = 5f
            draw.centerRadius = 30f
            draw.start()
            binding.tvPostName.text = item.title
            // TODO добавить всем картинкам в проекте Glide
            Glide.with(binding.root).load(Uri.parse(item.photoUrl)).placeholder(draw).into(binding.postImage)
            when (item.inFeatured) {
                true -> binding.featuredImage.load(R.drawable.ic_heart_fill)
                false -> binding.featuredImage.load(R.drawable.ic_heart_line)
            }
            binding.featuredImage.setOnClickListener { featuredClickListener(item) }
            binding.root.setOnClickListener { navigateClickListener(item) }
        }
    }
}