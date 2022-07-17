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

class SplashFragment : Fragment() {

    private lateinit var splashViewModelFactory: SplashViewModelFactory
    private val viewModel: SplashViewModel by viewModels { splashViewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        getViewModelFactory()

        viewModel.userToken.observe(viewLifecycleOwner) { token ->
            token?.let {
                 viewModel.setLoginStatus(it)
            }
        }
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, 500) // TODO проверить сколько по тз
    }

    private fun getViewModelFactory() {
        val application = requireNotNull(this.activity).application
        val database = PostDatabase.getInstance(application)
        val userTokenDao = database.userTokenDao
        splashViewModelFactory = SplashViewModelFactory(userTokenDao)
    }

    private fun navigateToNextScreen() {
        val nextScreenDestination = getNextScreenDestination()
        findNavController().navigate(nextScreenDestination)
    }

    private fun getNextScreenDestination(): Int {
        return if (viewModel.isLoggedIn.value == SplashViewModel.NOT_LOGGED_IN) {
            R.id.action_splashFragment_to_loginFragment
        } else {
            R.id.action_splashFragment_to_mainFragment
        }
    }
}