package ru.surf.gallery.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import ru.surf.gallery.database.PostDatabase
import ru.surf.gallery.databinding.FragmentPostBinding


class PostFragment : Fragment() {

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
        val application = requireNotNull(this.activity).application
        val postDao = PostDatabase.getInstance(application).postDao
        postViewModelFactory = PostViewModelFactory(postId, postDao)
    }

    private fun setBackArrowClickListener() {
        binding.backArrowImage.setOnClickListener {
            returnToPreviousScreen()
        }
    }

    private fun returnToPreviousScreen() {
        findNavController().popBackStack()
    }

    private fun observePost() {
        viewModel.post.observe(viewLifecycleOwner) { post ->
            post?.let {
                binding.name.text = post.title
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