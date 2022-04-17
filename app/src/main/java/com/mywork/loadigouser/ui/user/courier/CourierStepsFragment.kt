package com.mywork.loadigouser.ui.user.courier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.gson.Gson
import com.mywork.loadigouser.R
import com.mywork.loadigouser.base.BaseFragment
import com.mywork.loadigouser.databinding.FragmentCourierStepsBinding
import com.mywork.loadigouser.databinding.FragmentFuelBinding
import com.mywork.loadigouser.model.locale.User
import com.mywork.loadigouser.ui.user.UserActivity
import com.mywork.loadigouser.ui.user.main.MainViewModel
import com.mywork.loadigouser.ui.user.main.ServicesAdapter
import com.mywork.loadigouser.util.Language
import com.mywork.loadigouser.util.LocalNotificationType
import com.mywork.loadigouser.util.Resource
import com.skydoves.powerspinner.SpinnerGravity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CourierStepsFragment : BaseFragment() {
    private var _binding: FragmentCourierStepsBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var user: User

    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var latitude:Double = 0.0
    private var longitude:Double = 0.0
    lateinit var mGoogleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_courier_steps, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        lifecycleScope.launch { viewModel.getServices()}

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.lifecycleOwner = this

        binding.tvPickup.text = user.pickupLocation

        binding.tvDropOff.text = user.deliveryLocation


        val isEnglish = sharedPreferenceCache.getLanguage()!! == Language.ENGLISH.lang

        binding.spinnerWeight.apply {
            arrowGravity = if (isEnglish) SpinnerGravity.END else SpinnerGravity.START

        }

        binding.btnContinue.setOnClickListener {
            navController.navigate(R.id.action_courierStepsFragment_to_pickupAndPaymentFragment)
        }



        observeLiveData()



    }

    override fun onResume() {
        super.onResume()
        (activity as UserActivity).binding.iHeader.tvTitle.text = getString(R.string.pick_delivery)
        (activity as UserActivity).binding.iHeader.btnBack.visibility = View.VISIBLE
        (activity as UserActivity).binding.iHeader.clHeader.visibility = View.VISIBLE
        (activity as UserActivity).binding.iHeader.btnBell.visibility = View.VISIBLE

        binding.tvPickup.setOnClickListener {
            it.isSelected = true
        }
        binding.tvDropOff.setOnClickListener {
            it.isSelected = true
        }


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