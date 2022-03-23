package com.mywork.loadigouser.ui.auth.login

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.mywork.loadigouser.R
import com.mywork.loadigouser.base.BaseFragment
import com.mywork.loadigouser.databinding.FragmentLoginBinding
import com.mywork.loadigouser.model.remote.request.auth.LoginRequest
import com.mywork.loadigouser.ui.user.UserActivity
import com.mywork.loadigouser.util.Constants.REQUEST_CODE
import com.mywork.loadigouser.util.LocalNotificationType
import com.mywork.loadigouser.util.Resource
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment(), EasyPermissions.PermissionCallbacks {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.lifecycleOwner = this

        observeLiveData()
        handleClicks()

        if (hasPermissions())
            getMobileNumber()
        else
            requestPermissions()
    }

    private fun handleClicks() {
        binding.btnSignIn.setOnClickListener {
            val mobileNumber = binding.etPhone.text.toString()
            val password = binding.etPassword.text.toString()
            if (checkValidation(mobileNumber, password)) {
                lifecycleScope.launch { viewModel.loginUser(LoginRequest(mobileNumber, password)) }
            }
        }

        binding.btnSignUp.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
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
                    sharedPreferenceCache.saveAuthToken(response.data?.accessToken!!)
                    sharedPreferenceCache.saveUser(response.data.user)
                    startActivity(Intent(requireContext(), UserActivity::class.java))
                    requireActivity().finish()
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

    @SuppressLint("MissingPermission")
    private fun getMobileNumber() {
        val manager: TelephonyManager =
            activity?.getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager
        if (manager.line1Number != null)
            binding.etPhone.setText(manager.line1Number.drop(2))
    }

    private fun hasPermissions() =
        EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.READ_PHONE_NUMBERS
        )

    private fun requestPermissions() {
        EasyPermissions.requestPermissions(
            host = this,
            rationale = getString(R.string.permission_string),
            requestCode = REQUEST_CODE,
            Manifest.permission.READ_PHONE_NUMBERS
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        getMobileNumber()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}