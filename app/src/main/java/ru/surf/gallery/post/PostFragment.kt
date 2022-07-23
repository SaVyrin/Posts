package ru.surf.gallery.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import ru.surf.gallery.database.PostDao
import ru.surf.gallery.databinding.FragmentPostBinding
import javax.inject.Inject

@AndroidEntryPoint
class PostFragment : Fragment() {

    @Inject lateinit var postDao: PostDao
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
                binding.nameTv.text = post.title
                binding.content.text = post.content
                binding.date.text = post.publicationDate
                binding.image.load(post.photoUrl)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}