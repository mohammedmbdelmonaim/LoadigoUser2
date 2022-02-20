package com.mywork.loadigouser.ui.auth.complete

import android.os.Bundle
import android.util.Patterns
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
import com.mywork.loadigouser.databinding.FragmentCompleteBinding
import com.mywork.loadigouser.databinding.FragmentLoginBinding
import com.mywork.loadigouser.model.remote.request.auth.CompleteRequest
import com.mywork.loadigouser.model.remote.request.auth.LoginRequest
import com.mywork.loadigouser.util.LocalNotificationType
import com.mywork.loadigouser.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompleteFragment : BaseFragment() {
    private var _binding: FragmentCompleteBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private val viewModel: CompleteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_complete, container, false)
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
            val fullName = binding.etFullName.text.toString()
            val email = binding.etEmail.text.toString()
            if (checkValidation(fullName, email)) {
                lifecycleScope.launch { viewModel.completeUser(CompleteRequest(fullName, email)) }
            }
        }
    }

    private fun observeLiveData() {
        viewModel.completeLiveData.observe(viewLifecycleOwner) { response ->
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

    private fun checkValidation(fullName: String, email: String): Boolean {
        if (fullName.isNullOrEmpty()) {
            binding.etFullName.error = getString(R.string.fullname_is_required)
            return false
        }

        if (email.isNullOrEmpty()) {
            binding.etEmail.error = getString(R.string.email_is_required)
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = getString(R.string.email_not_valid)
            return false
        }

        return true
    }
}