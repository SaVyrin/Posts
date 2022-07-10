package ru.surf.gallery.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.surf.gallery.Post
import ru.surf.gallery.R

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
        with(recyclerView) {
            val mainAdapter = MainPostRecyclerViewAdapter {
                print("sdfsd")
                findNavController().navigate(R.id.action_mainFragment_to_postFragment)
            }
            mainAdapter.submitList(
                listOf(
                    Post(1, "1stName"),
                    Post(2, "2ndName"),
                    Post(3, "3rdName"),
                    Post(4, "4thName"),
                    Post(5, "5thName"),
                    Post(6, "6thName"),
                    Post(7, "7thName"),
                    Post(8, "8thName"),
                )
            )
            adapter = mainAdapter
        }

        return view
    }
}