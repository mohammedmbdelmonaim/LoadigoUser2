package com.mywork.loadigouser.ui.user.pick_delivery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.mywork.loadigouser.R
import com.mywork.loadigouser.base.BaseFragment
import com.mywork.loadigouser.databinding.FragmentPickDeliveryBinding
import com.mywork.loadigouser.model.locale.User
import com.mywork.loadigouser.ui.user.UserActivity
import com.mywork.loadigouser.ui.user.categories.CategoriesViewModel
import com.mywork.loadigouser.util.LocalNotificationType
import com.mywork.loadigouser.util.LocationType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PickAndDeliveryFragment : BaseFragment() {
    private var _binding: FragmentPickDeliveryBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    @Inject
    lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_pick_delivery,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.lifecycleOwner = this

    }

    override fun onResume() {
        super.onResume()
        (activity as UserActivity).binding.iHeader.tvTitle.text = getString(R.string.pick_delivery)
        (activity as UserActivity).binding.iHeader.btnBack.visibility = View.VISIBLE
        (activity as UserActivity).binding.iHeader.clHeader.visibility = View.VISIBLE
        (activity as UserActivity).binding.iHeader.btnBell.visibility = View.VISIBLE

        binding.mcPick.setOnClickListener {
            val action =
                PickAndDeliveryFragmentDirections.actionPickAndDeliveryFragmentToMapFragment(
                    LocationType.PICK.type
                )
            navController.navigate(action)
        }
        binding.mcDeliver.setOnClickListener {
            val action =
                PickAndDeliveryFragmentDirections.actionPickAndDeliveryFragmentToMapFragment(
                    LocationType.DELIVERY.type
                )
            navController.navigate(action)
        }


        binding.ivPick.isVisible = !user.pickupLocation.isNullOrEmpty()
        binding.ivDrop.isVisible = !user.deliveryLocation.isNullOrEmpty()
        binding.btnContinue.setOnClickListener {
            when {
                user.pickupLocation.isNullOrEmpty() -> {
                    showLocalNotification(
                        LocalNotificationType.ERROR,
                        getString(R.string.select_pickup)
                    )
                    return@setOnClickListener
                }
                user.deliveryLocation.isNullOrEmpty() -> {
                    showLocalNotification(
                        LocalNotificationType.ERROR,
                        getString(R.string.select_dropoff)
                    )
                    return@setOnClickListener
                }
                else -> {
                    navController.navigate(R.id.action_pickAndDeliveryFragment_to_courierStepsFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}