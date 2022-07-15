package ru.surf.gallery.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import ru.surf.gallery.database.PostDatabase
import ru.surf.gallery.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var profileViewModelFactory: ProfileViewModelFactory
    private val viewModel: ProfileViewModel by viewModels { profileViewModelFactory }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(this.activity).application
        val userDao = PostDatabase.getInstance(application).userDao
        profileViewModelFactory = ProfileViewModelFactory(userDao)


        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                val user = it[0]
                binding.avatar.load(user.avatar)
                binding.name.text = "${user.firstName} ${user.lastName}"
                binding.about.text = user.about
                binding.phone.text = user.phone
                binding.city.text = user.city
                binding.email.text = user.email
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}