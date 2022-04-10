package com.mywork.loadigouser.ui.user.map

import android.Manifest
import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.mywork.loadigouser.R
import com.mywork.loadigouser.base.BaseFragment
import com.mywork.loadigouser.databinding.FragmentFuelBinding
import com.mywork.loadigouser.databinding.FragmentMainBinding
import com.mywork.loadigouser.databinding.FragmentMapBinding
import com.mywork.loadigouser.model.locale.User
import com.mywork.loadigouser.ui.user.UserActivity
import com.mywork.loadigouser.ui.user.main.MainViewModel
import com.mywork.loadigouser.ui.user.main.ServicesAdapter
import com.mywork.loadigouser.util.*
import com.mywork.loadigouser.util.extensions.checkGPSEnabledAndShowRationale
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : BaseFragment(), OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener,
    EasyPermissions.PermissionCallbacks {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()
    private val args: MapFragmentArgs by navArgs()
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
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_map, container, false)
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


        binding.btnEnterManual.setOnClickListener {
            if (manual) {
                mGoogleMap.uiSettings.isScrollGesturesEnabled = true
                binding.tvLocation.isEnabled = false
                getCurrentLocation()
                binding.btnEnterManual.text = getString(R.string.enter_manual)
                manual = false
            } else {
                mGoogleMap.uiSettings.isScrollGesturesEnabled = false
                requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
                KeyboardUtils.showSoftKeyboard(requireContext(), binding.tvLocation)
                binding.tvLocation.isEnabled = true
                binding.tvLocation.setText("")
                binding.btnEnterManual.text = getString(R.string.map)
                latitude = 0.0
                longitude = 0.0
                manual = true
            }
        }


        binding.btnContinue.setOnClickListener {
            if (args.locationType == LocationType.PICK.type) {
                user.pickupLocation = binding.tvLocation.text.toString()
                if (latitude == 0.0) user.pickupLat = null else user.pickupLat = latitude
                if (longitude == 0.0) user.pickupLong = null else user.pickupLong = longitude
            } else {
                user.deliveryLocation = binding.tvLocation.text.toString()
                if (latitude == 0.0) user.deliveryLat = null else user.deliveryLat = latitude
                if (longitude == 0.0) user.deliveryLong = null else user.deliveryLong = longitude
            }
            sharedPreferenceCache.saveUser(user)
            navController.navigateUp()
        }

    }

    override fun onResume() {
        super.onResume()

        when (args.locationType) {
            LocationType.PICK.type -> (activity as UserActivity).binding.iHeader.tvTitle.text = getString(R.string.pickup_location)
            LocationType.PICK.type -> (activity as UserActivity).binding.iHeader.tvTitle.text = getString(
                R.string.delivery_location
            )
            else -> (activity as UserActivity).binding.iHeader.tvTitle.text = getString(R.string.address_book)
        }
        (activity as UserActivity).binding.iHeader.btnBack.visibility = View.VISIBLE
        (activity as UserActivity).binding.iHeader.clHeader.visibility = View.VISIBLE
        (activity as UserActivity).binding.iHeader.btnBell.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            getCompleteAddressString(latitude, longitude)
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

        getCompleteAddressString(latitude, longitude)
    }


    private fun getCompleteAddressString(latitude: Double, longitude: Double) {
        val geoCoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses: List<Address>? = geoCoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null) {
                val returnedAddress: Address = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                binding.tvLocation.setText(strReturnedAddress.toString())
            } else {
                binding.tvLocation.setText(getString(R.string.no_address_returned))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            binding.tvLocation.setText(getString(R.string.no_address_returned))
        }
    }


}