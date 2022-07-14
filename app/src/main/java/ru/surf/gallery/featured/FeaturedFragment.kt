package ru.surf.gallery.featured

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.surf.gallery.database.Post
import ru.surf.gallery.R
import ru.surf.gallery.databinding.FragmentFeaturedListBinding
import ru.surf.gallery.databinding.FragmentMainListBinding

class FeaturedFragment : Fragment() {

    private var _binding: FragmentFeaturedListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeaturedListBinding.inflate(inflater, container, false)
        val view = binding.root

        with(binding.list) {
            val featuredAdapter = FeaturedPostRecyclerViewAdapter {
                print("sdfsd")
                findNavController().navigate(R.id.action_featuredFragment_to_postFragment)
            }
            featuredAdapter.submitList(
                listOf(
                    Post("1", "1stName"),
                    Post("2", "2ndName"),
                    Post("3", "3rdName"),
                    Post("4", "4thName"),
                    Post("5", "5thName"),
                    Post("6", "6thName"),
                    Post("7", "7thName"),
                    Post("8", "8thName"),
                )
            )
            adapter = featuredAdapter
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}