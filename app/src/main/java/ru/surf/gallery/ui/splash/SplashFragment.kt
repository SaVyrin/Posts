package ru.surf.gallery.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.surf.gallery.R
import ru.surf.gallery.ui.login.LoginStatus

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUserToken()
        navigateToNextScreenWithDelay()
    }

    private fun observeUserToken() {
        viewModel.userToken.observe(viewLifecycleOwner) { token ->
            token?.let {
                viewModel.setLoginStatus()
            }
        }
    }

    private fun navigateToNextScreenWithDelay() {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                val nextScreenDestination = getNextScreenDestination()
                findNavController().navigate(nextScreenDestination)
            }, 800
        )
    }


    private fun getNextScreenDestination(): Int {
        return when (viewModel.logInStatus.value) {
            LoginStatus.LOGGED_IN -> R.id.action_splashFragment_to_mainFragment
            else -> R.id.action_splashFragment_to_loginFragment
        }
    }
}