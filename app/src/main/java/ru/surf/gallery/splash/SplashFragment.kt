package ru.surf.gallery.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.surf.gallery.R
import ru.surf.gallery.database.PostDatabase
import ru.surf.gallery.login.LoginStatus

class SplashFragment : Fragment() {

    private lateinit var splashViewModelFactory: SplashViewModelFactory
    private val viewModel: SplashViewModel by viewModels { splashViewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getViewModelFactory()
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUserToken()
        navigateToNextScreenWithDelay()
    }

    private fun getViewModelFactory() {
        val application = requireNotNull(this.activity).application
        val database = PostDatabase.getInstance(application)
        val userTokenDao = database.userTokenDao
        splashViewModelFactory = SplashViewModelFactory(userTokenDao)
    }

    private fun observeUserToken() {
        viewModel.userToken.observe(viewLifecycleOwner) { token ->
            token?.let {
                viewModel.setLoginStatus(it)
            }
        }
    }

    private fun navigateToNextScreenWithDelay() {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                val nextScreenDestination = getNextScreenDestination()
                findNavController().navigate(nextScreenDestination)
            }, 800
        ) // TODO проверить сколько по тз
    }


    private fun getNextScreenDestination(): Int {
        return when (viewModel.logInStatus.value) {
            LoginStatus.LOGGED_IN -> R.id.action_splashFragment_to_mainFragment
            else -> R.id.action_splashFragment_to_loginFragment
        }
    }
}