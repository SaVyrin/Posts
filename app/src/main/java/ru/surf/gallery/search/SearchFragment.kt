package ru.surf.gallery.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
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
        binding.searchViewEt.doOnTextChanged { newText, _, _, _ ->
            newText?.let {
                viewModel.findMatchingPosts(newText.toString())
            }
        }
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
        binding.showResultsLayout.list.adapter = mainAdapter
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
        binding.notSearchingLayout.root.isVisible = true
        binding.showResultsLayout.root.isVisible = false
        binding.noResultsLayout.root.isVisible = false
    }

    private fun showNoResultsSearchState() {
        binding.notSearchingLayout.root.isVisible = false
        binding.noResultsLayout.root.isVisible = true
        binding.showResultsLayout.root.isVisible = false
    }

    private fun showSuccessfulSearchState() {
        binding.notSearchingLayout.root.isVisible = false
        binding.noResultsLayout.root.isVisible = false
        binding.showResultsLayout.root.isVisible = true
    }
}
