package com.inensus.android.login.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.inensus.android.R
import com.inensus.android.base.view.BaseFragment
import com.inensus.android.base.view.InputBottomSheetFragment
import com.inensus.android.databinding.FragmentLoginBinding
import com.inensus.android.login.viewmodel.LoginViewModel
import com.inensus.android.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment() {
    private val viewModel: LoginViewModel by viewModel()

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.uiState.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is LoginUiState.ServerUrl -> {
                        if (it.askForServerUrl) {
                            askForServerUrl()
                        }
                    }

                    is LoginUiState.Success -> {
                        startActivity(Intent(requireActivity(), MainActivity::class.java))
                        requireActivity().finish()
                    }

                    is LoginUiState.ValidationError -> {
                        handleValidationError(it.errors)
                    }
                }
            },
        )

        viewModel.email.observe(
            viewLifecycleOwner,
            Observer {
                if (binding.emailInput.getText() != it) {
                    binding.emailInput.setText(it)
                }
            },
        )

        viewModel.password.observe(
            viewLifecycleOwner,
            Observer {
                if (binding.passwordInput.getText() != it) {
                    binding.passwordInput.setText(it)
                }
            },
        )
    }

    private fun setupListeners() {
        binding.emailInput.afterTextChanged = { viewModel.onEmailChanged(it.toString()) }
        binding.passwordInput.afterTextChanged = { viewModel.onPasswordChanged(it.toString()) }
        binding.loginButton.setOnClickListener { viewModel.onLoginButtonTapped() }

        binding.forgotPasswordText.setOnClickListener {
            AlertDialog
                .Builder(requireContext())
                .setTitle(getString(R.string.warning))
                .setCancelable(false)
                .setMessage(getString(R.string.login_forgot_password_action))
                .setPositiveButton(getString(R.string.ok)) { _, _ -> }
                .show()
        }
    }

    private fun askForServerUrl() {
        InputBottomSheetFragment.newInstance().also {
            it.show(childFragmentManager, BOTTOM_SHEET_FRAGMENT_TAG)
        }
    }

    private fun handleValidationError(errors: List<LoginUiState.ValidationError.Error>) {
        errors.forEach { validationError ->
            when (validationError) {
                is LoginUiState.ValidationError.Error.EmailIsBlank,
                is LoginUiState.ValidationError.Error.EmailIsNotInCorrectFormat,
                -> {
                    binding.emailInput.setErrorState(true)
                }

                is LoginUiState.ValidationError.Error.PasswordIsBlank -> {
                    binding.passwordInput.setErrorState(true)
                }
            }
        }
    }

    override fun provideViewModel() = viewModel

    companion object {
        private const val BOTTOM_SHEET_FRAGMENT_TAG = "BOTTOM_SHEET_FRAGMENT_TAG"
    }
}
