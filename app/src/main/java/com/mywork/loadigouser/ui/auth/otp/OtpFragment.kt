package com.mywork.loadigouser.ui.auth.otp

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
import com.mywork.loadigouser.databinding.FragmentOtpBinding
import com.mywork.loadigouser.databinding.FragmentRegisterBinding
import com.mywork.loadigouser.model.remote.request.auth.LoginRequest
import com.mywork.loadigouser.util.LocalNotificationType
import com.mywork.loadigouser.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OtpFragment : BaseFragment() {
    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private val viewModel: OtpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_otp, container, false)
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
//        binding.btnSignIn.setOnClickListener {
//            val mobileNumber = binding.etPhone.text.toString()
//            val password = binding.etPassword.text.toString()
//            if (checkValidation(mobileNumber, password)) {
//                lifecycleScope.launch { viewModel.registerUser(LoginRequest(mobileNumber, password)) }
//            }
//        }

        binding.btnContinue.setOnClickListener {
            navController.navigate(R.id.action_otpFragment_to_completeFragment)
        }
    }

    private fun observeLiveData() {
        viewModel.otpLiveData.observe(viewLifecycleOwner) { response ->
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

}