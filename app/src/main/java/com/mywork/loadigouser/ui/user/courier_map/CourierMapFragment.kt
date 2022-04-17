package com.mywork.loadigouser.ui.user.courier_map

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.mywork.loadigouser.R
import com.mywork.loadigouser.base.BaseFragment
import com.mywork.loadigouser.databinding.FragmentCourierMapBinding
import com.mywork.loadigouser.model.locale.User
import com.mywork.loadigouser.ui.user.UserActivity
import com.mywork.loadigouser.ui.user.main.MainViewModel
import com.mywork.loadigouser.ui.user.main.ServicesAdapter
import com.mywork.loadigouser.util.Constants
import com.mywork.loadigouser.util.extensions.checkGPSEnabledAndShowRationale
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CourierMapFragment : BaseFragment(), ServicesAdapter.ClickListener, OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener,
    EasyPermissions.PermissionCallbacks {
    private var _binding: FragmentCourierMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    lateinit var mGoogleMap: GoogleMap
    private var manual = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_courier_map, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.lifecycleOwner = this

        var mapFrag: SupportMapFragment =
            (childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?)!!
        mapFrag?.getMapAsync(this)

        val userGson = sharedPreferenceCache.getUser()
        val user = Gson().fromJson(userGson, User::class.java)



//        binding.btnContinue.setOnClickListener {
//            if (args.locationType == LocationType.PICK.type) {
//                user.pickupLocation = binding.tvLocation.text.toString()
//                if (latitude == 0.0) user.pickupLat = null else user.pickupLat = latitude
//                if (longitude == 0.0) user.pickupLong = null else user.pickupLong = longitude
//            } else {
//                user.deliveryLocation = binding.tvLocation.text.toString()
//                if (latitude == 0.0) user.deliveryLat = null else user.deliveryLat = latitude
//                if (longitude == 0.0) user.deliveryLong = null else user.deliveryLong = longitude
//            }
//            sharedPreferenceCache.saveUser(user)
//            navController.navigateUp()
//        }

    }

    override fun onResume() {
        super.onResume()
        (activity as UserActivity).binding.iHeader.clHeader.visibility = View.GONE

        lifecycleScope.launch {
            delay(5000)
            withContext(Dispatchers.Main){
                binding.clMan.visibility = View.VISIBLE
                binding.txtMan.visibility = View.VISIBLE
                binding.llOrder.visibility = View.VISIBLE
                binding.llSearching.visibility = View.GONE
            }
        }

        binding.btnCall.setOnClickListener {
            navController.navigate(R.id.action_courierMapFragment_to_successFragment)
        }

        binding.btnMessage.setOnClickListener {
            navController.navigate(R.id.action_courierMapFragment_to_successFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickService(position: Int) {
        navController.navigate(R.id.action_mainFragment_to_categoriesFragment)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireContext().checkGPSEnabledAndShowRationale()
        }

        if (hasPermissions())
            getCurrentLocation()
        else
            requestPermissions()

        mGoogleMap.setOnCameraMoveListener(this)
        mGoogleMap.setOnCameraIdleListener(this)
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        mFusedLocationClient?.lastLocation?.addOnCompleteListener {
            latitude = it.result.latitude
            longitude = it.result.longitude
            val latLng = LatLng(latitude, longitude)

            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
        }
    }

    private fun hasPermissions() =
        EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    private fun requestPermissions() {
        EasyPermissions.requestPermissions(
            host = this,
            rationale = getString(R.string.permission_string),
            requestCode = Constants.REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        getCurrentLocation()
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

    override fun onCameraMove() {
        // mGoogleMap.clear()
        // iv_marker?.visibility = View.VISIBLE
    }

    override fun onCameraIdle() {
        latitude = mGoogleMap.cameraPosition.target.latitude
        longitude = mGoogleMap.cameraPosition.target.longitude


    }


}