package ru.surf.gallery.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.surf.gallery.R
import ru.surf.gallery.data.database.Post
import ru.surf.gallery.databinding.FragmentMainBinding


@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSearchClickListener()
        initRecyclerView()
        setErrorLoadButtonClickListener()
        setRefreshLayoutListener()

        observeUser()
        observePostsRequestStatus()
    }

    private fun setSearchClickListener() {
        binding.search.setOnMenuItemClickListener {
            navigateToSearchScreen()
            true
        }
    }

    private fun navigateToSearchScreen() {
        findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
    }

    private fun initRecyclerView() {
        val mainAdapter = MainPostRecyclerViewAdapter(
            featuredClickListener = { post ->
                featuredIconClicked(post)
            },
            navigateClickListener = { post ->
                navigateToPost(post)
            }
        )
        setRecyclerViewAdapter(mainAdapter)
        observePosts(mainAdapter)
    }

    private fun featuredIconClicked(post: Post) {
        viewModel.featuredIconClicked(post)
    }

    private fun navigateToPost(post: Post) {
        val action = MainFragmentDirections.actionMainFragmentToPostFragment(post.id)
        findNavController().navigate(action)
    }

    private fun setRecyclerViewAdapter(mainAdapter: MainPostRecyclerViewAdapter) {
        binding.mainListLayout.mainList.adapter = mainAdapter
    }

    private fun observePosts(mainAdapter: MainPostRecyclerViewAdapter) {
        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            posts?.let {
                mainAdapter.submitList(posts)
            }
        }
    }

    private fun setErrorLoadButtonClickListener() {
        binding.mainErrorLoadLayout.loadBtn.setOnClickListener {
            viewModel.getPosts()
        }
    }

    private fun setRefreshLayoutListener() {
        binding.mainListLayout.swipeRefreshLayout.setOnRefreshListener {
            refreshPosts()
        }
    }

    private fun refreshPosts() {
        viewModel.refreshPosts()
    }

    private fun observeUser() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let{
                viewModel.setUserId(user)
            }
        }
    }

    private fun observePostsRequestStatus() {
        viewModel.postsRequestStatus.observe(viewLifecycleOwner) { postsRequest ->
            postsRequest?.let {
                when (postsRequest) {
                    PostsRequestStatus.LOADING -> {
                        showLoadingScreenState()
                    }
                    PostsRequestStatus.SUCCESS -> {
                        showSuccessScreenState()
                    }
                    PostsRequestStatus.ERROR_LOAD -> {
                        showErrorLoadScreenState()
                    }
                    PostsRequestStatus.ERROR_REFRESH -> {
                        showErrorRefreshScreenState()
                        showErrorRefreshSnackbar()
                    }
                    PostsRequestStatus.UNAUTHORIZED -> {
                        navigateToLoginScreen()
                    }
                    PostsRequestStatus.REFRESHING -> {
                        // DO nothing
                    }
                }
            }
        }
    }

    private fun showLoadingScreenState() {
        binding.mainListLayout.root.isVisible = false
        binding.search.isVisible = false
        binding.mainLoaderLayout.root.isVisible = true
        binding.mainErrorLoadLayout.root.isVisible = false
    }

    private fun showSuccessScreenState() {
        binding.mainListLayout.root.isVisible = true
        binding.search.isVisible = true
        binding.mainLoaderLayout.root.isVisible = false
        binding.mainErrorLoadLayout.root.isVisible = false
        binding.mainListLayout.swipeRefreshLayout.isRefreshing = false
    }

    private fun showErrorLoadScreenState() {
        binding.mainListLayout.root.isVisible = false
        binding.search.isVisible = false
        binding.mainLoaderLayout.root.isVisible = false
        binding.mainErrorLoadLayout.root.isVisible = true
    }

    private fun showErrorRefreshScreenState() {
        binding.mainListLayout.root.isVisible = true
        binding.search.isVisible = true
        binding.mainLoaderLayout.root.isVisible = false
        binding.mainErrorLoadLayout.root.isVisible = false
        binding.mainListLayout.swipeRefreshLayout.isRefreshing = false
    }

    private fun showErrorRefreshSnackbar() {
        Snackbar
            .make(
                binding.root,
                R.string.main_screen_reload_error,
                Snackbar.LENGTH_LONG
            )
            .setAnchorView(
                requireActivity().findViewById(R.id.bottom_navigation_view)
            )
            .show()
    }

    private fun navigateToLoginScreen() {
        findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}