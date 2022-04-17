package com.mywork.loadigouser.ui.user.tow_truck

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
import com.mywork.loadigouser.databinding.FragmentFuelBinding
import com.mywork.loadigouser.databinding.FragmentTwoTruckBinding
import com.mywork.loadigouser.model.locale.User
import com.mywork.loadigouser.ui.user.UserActivity
import com.mywork.loadigouser.ui.user.main.MainViewModel
import com.mywork.loadigouser.ui.user.main.ServicesAdapter
import com.mywork.loadigouser.util.LocalNotificationType
import com.mywork.loadigouser.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TowTruckFragment : BaseFragment() {
    private var _binding: FragmentTwoTruckBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private val viewModel: TowTuckViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_two_truck, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.lifecycleOwner = this

        observeLiveData()

    }

    override fun onResume() {
        super.onResume()
        (activity as UserActivity).binding.iHeader.tvTitle.text = getString(R.string.tow_truck)
        (activity as UserActivity).binding.iHeader.btnBack.visibility = View.GONE
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