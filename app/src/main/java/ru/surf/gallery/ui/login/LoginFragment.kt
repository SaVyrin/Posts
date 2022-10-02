package ru.surf.gallery.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout.END_ICON_NONE
import com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.MaskedTextChangedListener.Companion.installOn
import dagger.hilt.android.AndroidEntryPoint
import ru.surf.gallery.R
import ru.surf.gallery.databinding.FargmentLoginBinding

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FargmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FargmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVSUDb()
        initLoginMask()
        setLoginButtonClickListener()
        observePasswordFieldChange()

        observeLoginStatus()
        observeLoginFieldStatus()
        observePasswordFieldStatus()
    }

    private fun initVSUDb() {
        viewModel.initVSUDB()
    }

    private fun initLoginMask() {
        installOn(
            binding.loginEdt,
            PHONE_MASK,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String
                ) {
                    viewModel.setLogin(extractedValue)
                }
            }
        )
    }

    private fun setLoginButtonClickListener() {
        binding.loginBtn.setOnClickListener {
            viewModel.logInUser()
        }
    }

    private fun observePasswordFieldChange() {
        binding.passwordEdt.doOnTextChanged { password, _, _, _ ->
            password?.let {
                viewModel.setPassword(password.toString())
                setPasswordFieldEndIconMode(password.toString())
            }
        }
    }

    private fun setPasswordFieldEndIconMode(password: String) {
        when (password.isNotEmpty()) {
            true -> binding.passwordTil.endIconMode = END_ICON_PASSWORD_TOGGLE
            else -> binding.passwordTil.endIconMode = END_ICON_NONE
        }
    }

    private fun observeLoginStatus() {
        viewModel.loginStatus.observe(viewLifecycleOwner) { loginStatus ->
            loginStatus?.let {
                when (loginStatus) {
                    LoginStatus.LOGGED_IN -> {
                        showLoggedInScreenState()
                        navigateToMainScreen()
                    }
                    LoginStatus.IN_PROGRESS -> {
                        showInProgressScreenState()
                    }
                    LoginStatus.ERROR_WRONG_DATA -> {
                        showErrorWrongDataScreenState()
                        showErrorSnackbar(R.string.wrong_login_or_password_error)
                    }
                    LoginStatus.ERROR_INTERNET -> {
                        showErrorNoInternetScreenState()
                        showErrorSnackbar(R.string.login_no_internet_error)
                    }
                    LoginStatus.NOT_LOGGED_IN -> {
                        // Do nothing
                    }
                }
            }
        }
    }

    private fun showLoggedInScreenState() {
        binding.loginBtn.isLoading = false
    }

    private fun navigateToMainScreen() {
        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
    }

    private fun showErrorWrongDataScreenState() {
        binding.loginBtn.isLoading = false
        binding.blockScreen.isVisible = false
        binding.loginTil.error = " "
        binding.passwordTil.error = " "
    }

    private fun showErrorNoInternetScreenState() {
        binding.loginBtn.isLoading = false
        binding.blockScreen.isVisible = false
    }

    private fun showErrorSnackbar(errorTextId: Int) {
        Snackbar.make(
            binding.root,
            errorTextId,
            Snackbar.LENGTH_LONG
        ).setAnchorView(binding.loginBtn).show()
    }

    private fun showInProgressScreenState() {
        binding.loginBtn.isLoading = true
        binding.blockScreen.isVisible = true
    }

    private fun observeLoginFieldStatus() {
        viewModel.loginFieldStatus.observe(viewLifecycleOwner) { loginFieldStatus ->
            loginFieldStatus?.let {
                when (loginFieldStatus) {
                    LoginFieldStatus.EMPTY -> {
                        showLoginEmptyFieldError()
                    }
                    LoginFieldStatus.NOT_VALID -> {
                        showLoginInvalidInputError()
                    }
                    LoginFieldStatus.VALID -> {
                        showLoginValidState()
                    }
                }
            }
        }
    }

    private fun showLoginEmptyFieldError() {
        binding.loginTil.error = getString(R.string.empty_field_error)
    }

    private fun showLoginInvalidInputError() {
        binding.loginTil.error = getString(R.string.wrong_phone_number_format_error)
    }

    private fun showLoginValidState() {
        binding.loginTil.error = null
        binding.loginTil.isErrorEnabled = false
    }

    private fun observePasswordFieldStatus() {
        viewModel.passwordFieldStatus.observe(viewLifecycleOwner) { passwordFieldStatus ->
            passwordFieldStatus?.let {
                when (passwordFieldStatus) {
                    PasswordFieldStatus.EMPTY -> {
                        showPasswordEmptyFieldError()
                    }
                    PasswordFieldStatus.NOT_VALID -> {
                        showPasswordInvalidInputError()
                    }
                    PasswordFieldStatus.VALID -> {
                        showPasswordValidState()
                    }
                }
            }
        }
    }

    private fun showPasswordEmptyFieldError() {
        binding.passwordTil.error = getString(R.string.empty_field_error)
    }

    private fun showPasswordInvalidInputError() {
        binding.passwordTil.error = getString(R.string.wrong_password_format_error)
    }

    private fun showPasswordValidState() {
        binding.passwordTil.error = null
        binding.passwordTil.isErrorEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val PHONE_MASK = "+7 ([000]) [000] [00] [00]"
    }
}