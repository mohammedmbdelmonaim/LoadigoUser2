package com.mywork.loadigouser.ui.user.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.mywork.loadigouser.R
import com.mywork.loadigouser.base.BaseFragment
import com.mywork.loadigouser.databinding.FragmentMainBinding
import com.mywork.loadigouser.databinding.FragmentProfileBinding
import com.mywork.loadigouser.databinding.FragmentRegisterBinding
import com.mywork.loadigouser.model.locale.User
import com.mywork.loadigouser.model.remote.request.auth.LoginRequest
import com.mywork.loadigouser.model.remote.response.user.ServiceResponse
import com.mywork.loadigouser.ui.auth.AuthActivity
import com.mywork.loadigouser.ui.boarding.OnBoardingActivity
import com.mywork.loadigouser.ui.dialogs.ConfirmInfoDialog
import com.mywork.loadigouser.ui.splash.SplashActivity
import com.mywork.loadigouser.ui.user.UserActivity
import com.mywork.loadigouser.ui.user.profile.language.BottomSheetLanguage
import com.mywork.loadigouser.util.Language
import com.mywork.loadigouser.util.LocalNotificationType
import com.mywork.loadigouser.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.lifecycleOwner = this
        observeLiveData()
        fillData()
    }

    private fun fillData() {
        binding.user = user

        if (sharedPreferenceCache.getLanguage() == Language.ENGLISH.lang) binding.tvLanguage.text = getString(R.string.english) else binding.tvLanguage.text = getString(
                    R.string.arabic)

        binding.tvLogout.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                logoutDialog()
            }
        }
        binding.tvLanguage.setOnClickListener {
            val bottomLangsSheet: BottomSheetLanguage = BottomSheetLanguage
                .newInstance(sharedPreferenceCache.getLanguage()!!)
            bottomLangsSheet.show(childFragmentManager, "")
            bottomLangsSheet.setOnItemBottomSheetClick {lang ->
                sharedPreferenceCache.saveLanguage(lang)
                startActivity(Intent(requireContext() , SplashActivity::class.java))
                requireActivity().finish()
            }
        }

        binding.tvOrderHistory.setOnClickListener {
            navController.navigate(R.id.action_profileFragment_to_historyFragment)
        }

        binding.tvDeliveryAddresses.setOnClickListener {
            navController.navigate(R.id.action_profileFragment_to_mapFragment)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun logoutDialog() {
        ConfirmInfoDialog(
            context = requireContext(),
            info = getString(R.string.logout_msg),
            positiveText = getString(R.string.logout),
            showPositiveButton = true,
            positiveClickAction = {
                sharedPreferenceCache.saveAuthToken(null)
                sharedPreferenceCache.saveUser(null)
                startActivity(Intent(requireContext() , AuthActivity::class.java))
                requireActivity().finish()
            },
            negativeText = getString(R.string.cancel),
            showNegativeButton = true,
            negativeClickAction = {
            },
            isCancelable = true
        ).show()
    }

    override fun onResume() {
        super.onResume()
        (activity as UserActivity).binding.iHeader.tvTitle.text = getString(R.string.profile)
        (activity as UserActivity).binding.iHeader.btnBack.visibility = View.VISIBLE
        (activity as UserActivity).binding.iHeader.clHeader.visibility = View.VISIBLE
        (activity as UserActivity).binding.iHeader.btnBell.visibility = View.VISIBLE
    }


    private fun observeLiveData() {
        viewModel.servicesLiveData.observe(viewLifecycleOwner) { response ->
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