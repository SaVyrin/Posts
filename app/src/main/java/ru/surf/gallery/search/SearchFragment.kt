package ru.surf.gallery.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import ru.surf.gallery.R
import ru.surf.gallery.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModels()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackArrowClickListener()
        setSearchViewTextListener()
        observeSearchText()
        observeSearchStatus()
    }

    private fun setBackArrowClickListener() {
        binding.backArrowImage.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setSearchViewTextListener() {
        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.setSearchText(newText)
                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    // task HERE
                    return false
                }

            })
    }

    private fun observeSearchText() {
        viewModel.searchText.observe(viewLifecycleOwner) { searchText ->
            searchText?.let {
                viewModel.setSearchStatus(searchText)
            }
        }
    }

    private fun observeSearchStatus() {
        viewModel.searchStatus.observe(viewLifecycleOwner) { searchStatus ->
            searchStatus?.let {
                when (searchStatus) {
                    SearchStatus.NOT_SEARCHING -> { // TODO подумать как написать получше
                        Navigation.findNavController(binding.navHostFragment)
                            .navigate(R.id.defaultSearchFragment)
                    }
                    else -> {
                        Navigation.findNavController(binding.navHostFragment)
                            .navigate(R.id.postsFragment)
                    }
                }
            }
        }
    }
}
