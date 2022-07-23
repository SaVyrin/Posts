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

        observeUserToken()
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
        binding.mainList.list.adapter = mainAdapter
    }

    private fun observePosts(mainAdapter: MainPostRecyclerViewAdapter) {
        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            posts?.let {
                mainAdapter.submitList(posts)
            }
        }
    }

    private fun setErrorLoadButtonClickListener() {
        binding.mainErrorLoad.btnLoad.setOnClickListener {
            viewModel.getPosts()
        }
    }

    private fun setRefreshLayoutListener() {
        binding.mainList.swipeRefreshLayout.setOnRefreshListener {
            refreshPosts()
        }
    }

    private fun refreshPosts() {
        viewModel.refreshPosts()
    }

    private fun observeUserToken() {
        viewModel.userTokenFromDao.observe(viewLifecycleOwner) { userToken ->
            userToken?.let {
                viewModel.setUserToken(userToken)
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
        binding.mainList.root.isVisible = false
        binding.search.isVisible = false
        binding.mainLoader.root.isVisible = true
        binding.mainErrorLoad.root.isVisible = false
    }

    private fun showSuccessScreenState() {
        binding.mainList.root.isVisible = true
        binding.search.isVisible = true
        binding.mainLoader.root.isVisible = false
        binding.mainErrorLoad.root.isVisible = false
        binding.mainList.swipeRefreshLayout.isRefreshing = false
    }

    private fun showErrorLoadScreenState() {
        binding.mainList.root.isVisible = false
        binding.search.isVisible = false
        binding.mainLoader.root.isVisible = false
        binding.mainErrorLoad.root.isVisible = true
    }

    private fun showErrorRefreshScreenState() {
        binding.mainList.root.isVisible = true
        binding.search.isVisible = true
        binding.mainLoader.root.isVisible = false
        binding.mainErrorLoad.root.isVisible = false
        binding.mainList.swipeRefreshLayout.isRefreshing = false
    }

    private fun showErrorRefreshSnackbar() {
        Snackbar
            .make(
                binding.root,
                R.string.main_screen_reload_error,
                Snackbar.LENGTH_LONG
            )
            .setAnchorView(
                requireActivity().findViewById(R.id.bottomNavigationView3)
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