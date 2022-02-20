package com.mywork.loadigouser.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.mywork.loadigouser.R
import com.mywork.loadigouser.base.BaseFragment
import com.mywork.loadigouser.databinding.FragmentRegisterBinding
import com.mywork.loadigouser.model.remote.request.auth.LoginRequest
import com.mywork.loadigouser.util.LocalNotificationType
import com.mywork.loadigouser.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_register, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.lifecycleOwner = this

        observeLiveData()
        handleClicks()
    }

    private fun handleClicks() {
        binding.btnSignIn.setOnClickListener {
            val mobileNumber = binding.etPhone.text.toString()
            val password = binding.etPassword.text.toString()
            if (checkValidation(mobileNumber, password)) {
                lifecycleScope.launch { viewModel.registerUser(LoginRequest(mobileNumber, password)) }
            }
        }
    }

    private fun observeLiveData() {
        viewModel.loginLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoadingIndicator()
                }
                is Resource.Success -> {
                    hideLoadingIndicator()
                }

                is Resource.Error -> {
                    hideLoadingIndicator()
                    showLocalNotification(
                        LocalNotificationType.ERROR, response.message.toString()
                    )
                }
                else -> hideLoadingIndicator()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkValidation(mobileNumber: String, password: String): Boolean {
        if (mobileNumber.isNullOrEmpty()) {
            binding.etPhone.error = getString(R.string.please_fill_the_phone)
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = getString(R.string.password_is_required)
            return false
        }

        if (mobileNumber.length < 11) {
            binding.etPhone.error = getString(R.string.invalid_phone_number)
            return false
        }
        val firstThreeDigit = mobileNumber.take(3)
        if (firstThreeDigit != "010" && firstThreeDigit != "011" && firstThreeDigit != "012" && firstThreeDigit != "015") {
            binding.etPhone.error = getString(R.string.invalid_phone_number)
            return false
        }
        return true
    }
}