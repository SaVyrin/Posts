package ru.surf.gallery.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout.END_ICON_NONE
import com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.MaskedTextChangedListener.Companion.installOn
import kotlinx.coroutines.launch
import ru.surf.gallery.R
import ru.surf.gallery.database.PostDatabase
import ru.surf.gallery.databinding.FargmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var loginViewModelFactory: LoginViewModelFactory
    private val viewModel: LoginViewModel by viewModels { loginViewModelFactory }

    private var _binding: FargmentLoginBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FargmentLoginBinding.inflate(inflater, container, false)
        getViewModelFactory()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLoginMask()
        setLoginButtonClickListener()
        observePasswordFieldChange()

        observeLoginStatus()
        observeLoginFieldStatus()
        observePasswordFieldStatus()
    }

    private fun getViewModelFactory() {
        val application = requireNotNull(this.activity).application
        val database = PostDatabase.getInstance(application)
        val userTokenDao = database.userTokenDao
        val userDao = database.userDao
        loginViewModelFactory = LoginViewModelFactory(userTokenDao, userDao)
    }

    private fun initLoginMask() {
        installOn(
            binding.etLogin,
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
        binding.btnLogin.setOnClickListener {
            lifecycleScope.launch {
                viewModel.logInUser()
            }
        }
    }

    private fun observePasswordFieldChange() {
        binding.etPassword.doOnTextChanged { password, _, _, _ ->
            password?.let {
                viewModel.setPassword(password.toString())
                setPasswordFieldEndIconMode(password.toString())
            }
        }
    }

    private fun setPasswordFieldEndIconMode(password: String) {
        when (password.isNotEmpty()) {
            true -> binding.password.endIconMode = END_ICON_PASSWORD_TOGGLE
            else -> binding.password.endIconMode = END_ICON_NONE
        }
    }

    private fun observeLoginStatus() {
        viewModel.loginStatus.observe(viewLifecycleOwner) { loginStatus ->
            loginStatus?.let {
                when (loginStatus) {
                    LoginStatus.LOGGED_IN -> {
                        binding.btnLogin.isLoading = false
                        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                    }
                    LoginStatus.ERROR -> {
                        binding.btnLogin.isLoading = false
                        binding.blockScreen.isVisible = false
                        Snackbar.make(
                            binding.root,
                            R.string.wrong_login_or_password_error,
                            Snackbar.LENGTH_LONG
                        ).setAnchorView(binding.btnLogin).show()
                    }
                    LoginStatus.LOGIN_IN_PROGRESS -> {
                        binding.btnLogin.isLoading = true
                        binding.blockScreen.isVisible = true
                    }
                    LoginStatus.NOT_LOGGED_IN -> {
                        // Do nothing
                    }
                }
            }
        }
    }

    private fun observeLoginFieldStatus() {
        viewModel.loginFieldStatus.observe(viewLifecycleOwner) { loginFieldStatus ->
            loginFieldStatus?.let {
                when (loginFieldStatus) {
                    LoginFieldStatus.EMPTY -> {
                        binding.login.error = getString(R.string.empty_field_error)
                    }
                    LoginFieldStatus.NOT_VALID -> {
                        binding.login.error = getString(R.string.wrong_phone_number_format_error)
                    }
                    LoginFieldStatus.VALID -> {
                        binding.login.error = null
                    }
                }
            }
        }
    }

    private fun observePasswordFieldStatus() {
        viewModel.passwordFieldStatus.observe(viewLifecycleOwner) { passwordFieldStatus ->
            passwordFieldStatus?.let {
                when (passwordFieldStatus) {
                    PasswordFieldStatus.EMPTY -> {
                        binding.password.error = getString(R.string.empty_field_error)
                    }
                    PasswordFieldStatus.NOT_VALID -> {
                        binding.password.error = getString(R.string.wrong_password_format_error)
                    }
                    PasswordFieldStatus.VALID -> {
                        binding.password.error = null
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val PHONE_MASK = "+7 ([000]) [000] [00] [00]"
    }
}