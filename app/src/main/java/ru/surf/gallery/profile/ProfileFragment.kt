package ru.surf.gallery.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.surf.gallery.R
import ru.surf.gallery.databinding.FragmentProfileBinding
import ru.surf.gallery.dialog.ProfileConfirmationDialog
import ru.surf.gallery.utils.formatPhone
import ru.surf.gallery.utils.withQuotation

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLogoutButtonClickListener()
        observeUser()
        observerUserToken()
        observeLogoutStatus()
    }

    private fun setLogoutButtonClickListener() {
        binding.logoutBtn.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        ProfileConfirmationDialog {
            viewModel.logOutUser()
        }.show(
            childFragmentManager,
            ProfileConfirmationDialog.TAG
        )
    }

    private fun observeUser() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.avatar.load(user.avatar)
                binding.name.text = "${user.firstName} ${user.lastName}"
                binding.about.text = user.about.withQuotation()
                binding.phone.text = user.phone.formatPhone()
                binding.city.text = user.city
                binding.email.text = user.email
            }
        }
    }

    private fun observerUserToken() {
        viewModel.userToken.observe(viewLifecycleOwner) { userToken ->
            userToken?.let {
                viewModel.setUserToken(userToken)
            }
        }
    }

    private fun observeLogoutStatus() {
        viewModel.logoutStatus.observe(viewLifecycleOwner) { logoutStatus ->
            logoutStatus?.let {
                when (logoutStatus) {
                    LogoutStatus.LOGGED_OUT -> {
                        showLoggedOutScreenState()
                        navigateToLoginScreen()
                    }
                    LogoutStatus.IN_PROGRESS -> {
                        showInProgressScreenState()
                    }
                    LogoutStatus.ERROR -> {
                        showLogoutErrorScreenState()
                        showLogoutErrorSnackbar()
                    }
                    LogoutStatus.NOT_LOGGED_OUT -> {
                        // Do nothing
                    }
                }
            }
        }
    }

    private fun showLoggedOutScreenState() {
        binding.logoutBtn.isLoading = false
    }

    private fun navigateToLoginScreen() {
        findNavController().navigate(R.id.action_fragmentProfile_to_loginFragment)
    }

    private fun showInProgressScreenState() {
        binding.logoutBtn.isLoading = true
    }

    private fun showLogoutErrorScreenState() {
        binding.logoutBtn.isLoading = false
    }

    private fun showLogoutErrorSnackbar() {
        Snackbar.make(
            binding.root,
            R.string.can_not_logout_error,
            Snackbar.LENGTH_LONG
        ).setAnchorView(binding.logoutBtn).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}