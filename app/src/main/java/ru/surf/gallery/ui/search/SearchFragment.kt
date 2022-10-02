package ru.surf.gallery.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.surf.gallery.data.database.Post
import ru.surf.gallery.databinding.FragmentSearchBinding
import ru.surf.gallery.ui.main.MainPostRecyclerViewAdapter
import ru.surf.gallery.utils.hideKeyboard
import ru.surf.gallery.utils.showKeyboard

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBackArrowClickListener()
        setSearchViewTextListener()
        startSearch()
        initRecyclerView()
        addRecyclerViewOnScrollListener()

        observeUser()
        observePostsFromDao()
        observeSearchStatus()
    }

    private fun setBackArrowClickListener() {
        binding.backArrow.setNavigationOnClickListener {
            returnToMainScreen()
        }
    }

    private fun returnToMainScreen() {
        findNavController().popBackStack()
    }

    private fun setSearchViewTextListener() {
        binding.searchEdt.doOnTextChanged { newText, _, _, _ ->
            newText?.let {
                viewModel.findMatchingPosts(newText.toString())
            }
        }
    }

    private fun startSearch() {
        binding.searchEdt.requestFocus()
        binding.searchEdt.showKeyboard()
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

    private fun addRecyclerViewOnScrollListener() {
        binding.showResultsLayout.searchList.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                binding.searchEdt.clearFocus()
                binding.searchEdt.hideKeyboard()
            }
        })
    }

    private fun featuredIconClicked(post: Post) {
        viewModel.featuredIconClicked(post)
    }

    private fun navigateToPost(post: Post) {
        val action = SearchFragmentDirections.actionSearchFragmentToPostFragment(post.id)
        findNavController().navigate(action)
    }

    private fun setRecyclerViewAdapter(mainAdapter: MainPostRecyclerViewAdapter) {
        binding.showResultsLayout.searchList.adapter = mainAdapter
    }

    private fun observePostsToShow(mainAdapter: MainPostRecyclerViewAdapter) {
        viewModel.postsToShow.observe(viewLifecycleOwner) { posts ->
            posts?.let {
                mainAdapter.submitList(posts)
            }
        }
    }

    private fun observeUser() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                viewModel.setUserId(user)
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

    override fun onPause() {
        super.onPause()
        binding.searchEdt.hideKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
