package ru.surf.gallery.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.surf.gallery.R
import ru.surf.gallery.data.database.User
import ru.surf.gallery.databinding.FragmentProfileBinding
import ru.surf.gallery.common.dialogs.ProfileConfirmationDialog
import ru.surf.gallery.utils.formatPhone
import ru.surf.gallery.utils.getSmallPlaceholder
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
                setAvatar(user)
                setName(user)
                setAbout(user)
                setPhone(user)
                setCity(user)
                setEmail(user)
            }
        }
    }

    private fun setAvatar(user: User) {
        when (user.avatar.isEmpty()) {
            true -> binding.avatarImage.load(R.drawable.ic_default_avatar)
            false -> {
                val placeholder = getSmallPlaceholder(binding.root.context)
                binding.avatarImage.load(user.avatar) {
                    crossfade(true)
                    placeholder(placeholder)
                }
            }
        }
    }

    private fun setName(user: User) {
        when (user.firstName.isEmpty() && user.lastName.isEmpty()) {
            true -> binding.fullNameTv.text = getString(R.string.profile_screen_no_name_text)
            false -> binding.fullNameTv.text = "${user.firstName} ${user.lastName}"
        }
    }

    private fun setAbout(user: User) {
        when (user.about.isEmpty()) {
            true -> binding.aboutTv.isVisible = false
            false -> binding.aboutTv.text = user.about.withQuotation()
        }
    }

    private fun setPhone(user: User) {
        when (user.phone.isEmpty()) {
            true -> binding.phoneGroup.isVisible = false
            false -> binding.phoneTv.text = user.phone.formatPhone()
        }
    }

    private fun setCity(user: User) {
        when (user.city.isEmpty()) {
            true -> binding.cityGroup.isVisible = false
            false -> binding.cityTv.text = user.city
        }
    }

    private fun setEmail(user: User) {
        when (user.email.isEmpty()) {
            true -> binding.emailGroup.isVisible = false
            false -> binding.emailTv.text = user.email
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