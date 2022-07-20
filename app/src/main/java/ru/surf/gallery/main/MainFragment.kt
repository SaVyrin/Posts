package ru.surf.gallery.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.surf.gallery.R
import ru.surf.gallery.database.PostDatabase
import ru.surf.gallery.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var mainViewModelFactory: MainViewModelFactory
    private val viewModel: MainViewModel by viewModels { mainViewModelFactory }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        getViewModelFactory()
        // TODO добавить SwipeRefreshLayout
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSearchClickListener()
        setRecyclerViewAdapter()
        observeUserToken()
        observePostsRequestStatus()
    }

    private fun getViewModelFactory() {
        val application = requireNotNull(this.activity).application
        val database = PostDatabase.getInstance(application)
        val userTokenDao = database.userTokenDao
        val postDao = database.postDao
        mainViewModelFactory = MainViewModelFactory(userTokenDao, postDao)
    }

    private fun setRecyclerViewAdapter() {
        val mainAdapter = MainPostRecyclerViewAdapter(
            featuredClickListener = { post ->
                lifecycleScope.launch {
                    viewModel.featuredIconClicked(post)
                }
            },
            navigateClickListener = { post ->
                val action = MainFragmentDirections.actionMainFragmentToPostFragment(post.id)
                findNavController().navigate(action)
            }
        )
        binding.mainList.list.adapter = mainAdapter
        observePosts(mainAdapter)
    }

    private fun setSearchClickListener() {
        binding.imageView.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
        }
    }

    private fun observeUserToken() {
        viewModel.userToken.observe(viewLifecycleOwner) { userToken ->
            userToken?.let {
                val userToken = userToken[0]
                lifecycleScope.launch {
                    viewModel.getPosts(userToken.token)
                }
            }
        }
    }

    private fun observePostsRequestStatus() {
        viewModel.postsRequestStatus.observe(viewLifecycleOwner) { postsRequest ->
            postsRequest?.let {
                when (postsRequest) {
                    PostsRequestStatus.IN_PROGRESS -> {
                        binding.mainList.root.isVisible = false
                        binding.imageView.isVisible = false
                        binding.mainLoader.root.isVisible = true
                        binding.mainErrorLoad.root.isVisible = false
                    }
                    PostsRequestStatus.SUCCESS -> {
                        binding.mainList.root.isVisible = true
                        binding.imageView.isVisible = true
                        binding.mainLoader.root.isVisible = false
                        binding.mainErrorLoad.root.isVisible = false
                    }
                    PostsRequestStatus.ERROR_RELOAD -> {
                        binding.mainList.root.isVisible = true
                        binding.imageView.isVisible = true
                        binding.mainLoader.root.isVisible = false
                        binding.mainErrorLoad.root.isVisible = false
                        Snackbar.make(
                            binding.root,
                            R.string.main_screen_reload_error,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    PostsRequestStatus.ERROR_LOAD -> {
                        binding.mainList.root.isVisible = false
                        binding.imageView.isVisible = false
                        binding.mainLoader.root.isVisible = false
                        binding.mainErrorLoad.root.isVisible = true
                    }
                }
            }
        }
    }

    private fun observePosts(mainAdapter: MainPostRecyclerViewAdapter) {
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