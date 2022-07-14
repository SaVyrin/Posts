package ru.surf.gallery.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.surf.gallery.R
import ru.surf.gallery.database.PostDatabase
import ru.surf.gallery.databinding.FragmentMainListBinding

class MainFragment : Fragment() {

    private lateinit var mainViewModelFactory: MainViewModelFactory
    private val viewModel: MainViewModel by viewModels { mainViewModelFactory }

    private var _binding: FragmentMainListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainListBinding.inflate(inflater, container, false)
        val view = binding.root

        getViewModelFactory()
        setRecyclerViewAdapter()

        viewModel.userToken.observe(viewLifecycleOwner) { userToken ->
            userToken?.let {
                val userToken = userToken[0]
                lifecycleScope.launch {
                    viewModel.getPosts(userToken.token)
                }
            }
        }

        return view
    }

    private fun getViewModelFactory() {
        val application = requireNotNull(this.activity).application
        val database = PostDatabase.getInstance(application)
        val userTokenDao = database.userTokenDao
        val postDao = database.postDao
        mainViewModelFactory = MainViewModelFactory(userTokenDao, postDao)
    }

    private fun setRecyclerViewAdapter() {
        val mainAdapter = MainPostRecyclerViewAdapter {
            print("sdfsd")
            findNavController().navigate(R.id.action_mainFragment_to_postFragment)
        }

        binding.list.adapter = mainAdapter

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            posts?.let {
                mainAdapter.submitList(posts)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}