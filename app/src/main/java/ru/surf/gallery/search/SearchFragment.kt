package ru.surf.gallery.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.surf.gallery.database.Post
import ru.surf.gallery.database.PostDatabase
import ru.surf.gallery.databinding.FragmentSearchBinding
import ru.surf.gallery.main.MainPostRecyclerViewAdapter

class SearchFragment : Fragment() {

    private lateinit var searchViewModelFactory: SearchViewModelFactory
    private val viewModel: SearchViewModel by viewModels { searchViewModelFactory }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        getViewModelFactory()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBackArrowClickListener()
        setSearchViewTextListener()
        initRecyclerView()

        observePostsFromDao()
        observeSearchStatus()
    }

    private fun getViewModelFactory() {
        val application = requireNotNull(this.activity).application
        val postDao = PostDatabase.getInstance(application).postDao
        searchViewModelFactory = SearchViewModelFactory(postDao)
    }

    private fun setBackArrowClickListener() {
        binding.backArrowImage.setOnClickListener {
            returnToMainScreen()
        }
    }

    private fun returnToMainScreen() {
        findNavController().popBackStack()
    }

    private fun setSearchViewTextListener() {
        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.findMatchingPosts(newText)
                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    // task HERE
                    return false
                }
            })
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
        observePostsToShow(mainAdapter)
    }

    private fun featuredIconClicked(post: Post) {
        viewModel.featuredIconClicked(post)
    }

    private fun navigateToPost(post: Post) {
        val action = SearchFragmentDirections.actionSearchFragmentToPostFragment(post.id)
        findNavController().navigate(action)
    }

    private fun setRecyclerViewAdapter(mainAdapter: MainPostRecyclerViewAdapter) {
        binding.showResults.list.adapter = mainAdapter
    }

    private fun observePostsToShow(mainAdapter: MainPostRecyclerViewAdapter) {
        viewModel.postsToShow.observe(viewLifecycleOwner) { posts ->
            posts?.let {
                mainAdapter.submitList(posts)
            }
        }
    }

    private fun observePostsFromDao() {
        viewModel.postsFromDao.observe(viewLifecycleOwner) { posts ->
            posts?.let {
                viewModel.setPostsToMatch(posts)
            }
        }
    }

    private fun observeSearchStatus() {
        viewModel.searchStatus.observe(viewLifecycleOwner) { searchStatus ->
            searchStatus?.let {
                when (searchStatus) {
                    SearchStatus.NOT_SEARCHING -> {
                        showNotSearchingState()
                    }
                    SearchStatus.NO_RESULTS -> {
                        showNoResultsSearchState()
                    }
                    SearchStatus.SHOW_RESULTS -> {
                        showSuccessfulSearchState()
                    }
                }
            }
        }
    }

    private fun showNotSearchingState() {
        binding.notSearching.root.isVisible = true
        binding.showResults.root.isVisible = false
        binding.noResults.root.isVisible = false
    }

    private fun showNoResultsSearchState() {
        binding.notSearching.root.isVisible = false
        binding.noResults.root.isVisible = true
        binding.showResults.root.isVisible = false
    }

    private fun showSuccessfulSearchState() {
        binding.notSearching.root.isVisible = false
        binding.noResults.root.isVisible = false
        binding.showResults.root.isVisible = true
    }
}
