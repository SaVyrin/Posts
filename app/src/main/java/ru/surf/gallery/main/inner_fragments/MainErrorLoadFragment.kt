package ru.surf.gallery.main.inner_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.surf.gallery.R
import ru.surf.gallery.databinding.FragmentMainErrorLoadBinding

class MainErrorLoadFragment : Fragment() {

    val binding : FragmentMainErrorLoadBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_error_load, container, false)
    }
}