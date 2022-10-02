package ru.surf.gallery.ui.featured

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.surf.gallery.data.database.Post
import ru.surf.gallery.databinding.FragmentFeaturedBinding
import ru.surf.gallery.common.dialogs.FeaturedConfirmationDialog


@AndroidEntryPoint
class FeaturedFragment : Fragment() {

    private val viewModel: FeaturedViewModel by viewModels()

    private var _binding: FragmentFeaturedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeaturedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeFeaturedScreenState()
        observeUser()
    }

    private fun initRecyclerView() {
        val featuredAdapter = FeaturedPostRecyclerViewAdapter(
            featuredClickListener = { post ->
                showRemoveFromFeaturedDialog(post)
            },
            navigateClickListener = { post ->
                navigateToPost(post)
            }
        )
        setRecyclerViewAdapter(featuredAdapter)
        observeFeaturedPosts(featuredAdapter)
    }

    private fun showRemoveFromFeaturedDialog(post: Post) {
        FeaturedConfirmationDialog {
            viewModel.removePostFromFeatured(post)
        }.show(
            childFragmentManager,
            FeaturedConfirmationDialog.TAG
        )
    }

    private fun navigateToPost(post: Post) {
        val action = FeaturedFragmentDirections.actionFeaturedFragmentToPostFragment(post.id)
        findNavController().navigate(action)
    }

    private fun setRecyclerViewAdapter(featuredAdapter: FeaturedPostRecyclerViewAdapter) {
        binding.featuredList.adapter = featuredAdapter
    }

    private fun observeFeaturedPosts(featuredAdapter: FeaturedPostRecyclerViewAdapter) {
        viewModel.featuredPosts.observe(viewLifecycleOwner) { posts ->
            posts?.let {
                featuredAdapter.submitList(posts)
                viewModel.setFeaturedScreenState(posts)
            }
        }
    }

    private fun observeFeaturedScreenState() {
        viewModel.featuredScreenState.observe(viewLifecycleOwner) { featuredState ->
            when (featuredState) {
                FeaturedState.SHOW_POSTS -> {
                    showFeaturedShowPostsScreenState()
                }
                else -> {
                    showFeaturedNoPostsScreenState()
                }
            }
        }
    }

    private fun showFeaturedShowPostsScreenState() {
        binding.featuredList.isVisible = true
        binding.noPostsLayout.root.isVisible = false
    }

    private fun showFeaturedNoPostsScreenState() {
        binding.featuredList.isVisible = false
        binding.noPostsLayout.root.isVisible = true
    }

    private fun observeUser() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                viewModel.getPosts(user.token)
                viewModel.userId = user.token
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}