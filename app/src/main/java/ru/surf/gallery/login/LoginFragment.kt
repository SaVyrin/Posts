package ru.surf.gallery.login

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
import ru.surf.gallery.databinding.FargmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var  loginViewModelFactory: LoginViewModelFactory
    private val viewModel: LoginViewModel by viewModels { loginViewModelFactory }

    private var _binding: FargmentLoginBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FargmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(this.activity).application
        val userDao = PostDatabase.getInstance(application).userDao
        loginViewModelFactory = LoginViewModelFactory(userDao)

        binding.btnLogin.setOnClickListener {
            val login = binding.etLogin.text.toString()
            val password = binding.etPassword.text.toString()

            lifecycleScope.launch {
                viewModel.logInUser(login, password)
            }
        }

        viewModel.loginStatus.observe(viewLifecycleOwner) { newValue ->
            when (newValue) {
                LoginViewModel.LOGGED_IN -> {
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
            }
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}