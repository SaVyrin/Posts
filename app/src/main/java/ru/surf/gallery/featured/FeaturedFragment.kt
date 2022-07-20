package ru.surf.gallery.featured

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.surf.gallery.database.Post
import ru.surf.gallery.database.PostDatabase
import ru.surf.gallery.databinding.FragmentFeaturedListBinding
import ru.surf.gallery.dialog.FeaturedConfirmationDialog

class FeaturedFragment : Fragment() {

    private lateinit var featuredViewModelFactory: FeaturedViewModelFactory
    private val viewModel: FeaturedViewModel by viewModels { featuredViewModelFactory }

    private var _binding: FragmentFeaturedListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeaturedListBinding.inflate(inflater, container, false)
        getViewModelFactory()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun getViewModelFactory() {
        val application = requireNotNull(this.activity).application
        val postDao = PostDatabase.getInstance(application).postDao
        featuredViewModelFactory = FeaturedViewModelFactory(postDao)
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
        binding.list.adapter = featuredAdapter
    }

    private fun observeFeaturedPosts(featuredAdapter: FeaturedPostRecyclerViewAdapter) {
        viewModel.featuredPosts.observe(viewLifecycleOwner) { posts ->
            posts?.let {
                featuredAdapter.submitList(posts)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}