package ru.surf.gallery.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import ru.surf.gallery.data.database.Post
import ru.surf.gallery.data.database.PostDao
import ru.surf.gallery.databinding.FragmentPostBinding
import ru.surf.gallery.utils.getLargePlaceholder
import javax.inject.Inject

@AndroidEntryPoint
class PostFragment : Fragment() {

    @Inject
    lateinit var postDao: PostDao
    private lateinit var postViewModelFactory: PostViewModelFactory
    private val viewModel: PostViewModel by viewModels { postViewModelFactory }

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        val postId = getPostIdFromArguments()
        getViewModelFactory(postId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackArrowClickListener()
        observePost()
    }

    private fun getPostIdFromArguments(): String {
        return PostFragmentArgs.fromBundle(requireArguments()).postId
    }

    private fun getViewModelFactory(postId: String) {
        postViewModelFactory = PostViewModelFactory(postId, postDao)
    }

    private fun setBackArrowClickListener() {
        binding.backArrow.setNavigationOnClickListener {
            returnToPreviousScreen()
        }
    }

    private fun returnToPreviousScreen() {
        findNavController().popBackStack()
    }

    private fun observePost() {
        viewModel.post.observe(viewLifecycleOwner) { post ->
            post?.let {
                setPostTitle(post)
                setPossImage(post)
                setPostContent(post)
                setPostPublicationDate(post)
            }
        }
    }

    private fun setPostTitle(post: Post) {
        binding.postTitleTv.text = post.title
    }

    private fun setPossImage(post: Post) {
        val placeholder = getLargePlaceholder(binding.root.context)
        binding.postImage.load(post.photoUrl) {
            crossfade(true)
            placeholder(placeholder)
        }
    }

    private fun setPostContent(post: Post) {
        binding.postContentTv.text = post.content
    }

    private fun setPostPublicationDate(post: Post) {
        binding.postPublicationDateTv.text = post.publicationDate
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}