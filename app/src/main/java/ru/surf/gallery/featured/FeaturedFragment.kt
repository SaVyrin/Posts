package ru.surf.gallery.featured

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.surf.gallery.R
import ru.surf.gallery.database.PostDatabase
import ru.surf.gallery.databinding.FragmentFeaturedListBinding

class FeaturedFragment : Fragment() {

    private lateinit var featuredViewModelFactory: FeaturedViewModelFactory
    private val viewModel: FeaturedViewModel by viewModels { featuredViewModelFactory }

    private var _binding: FragmentFeaturedListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeaturedListBinding.inflate(inflater, container, false)
        val view = binding.root

        getViewModelFactory()
        setRecyclerViewAdapter()

        return view
    }

    private fun getViewModelFactory() {
        val application = requireNotNull(this.activity).application
        val postDao = PostDatabase.getInstance(application).postDao
        featuredViewModelFactory = FeaturedViewModelFactory(postDao)
    }

    private fun setRecyclerViewAdapter() {
        val featuredAdapter = FeaturedPostRecyclerViewAdapter {
            findNavController().navigate(R.id.action_featuredFragment_to_postFragment)
        }

        binding.list.adapter = featuredAdapter

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