package com.mywork.loadigouser.ui.user.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import com.mywork.loadigouser.R
import com.mywork.loadigouser.base.BaseFragment
import com.mywork.loadigouser.databinding.FragmentMainBinding
import com.mywork.loadigouser.databinding.FragmentRegisterBinding
import com.mywork.loadigouser.model.locale.User
import com.mywork.loadigouser.model.remote.request.auth.LoginRequest
import com.mywork.loadigouser.model.remote.response.user.ServiceResponse
import com.mywork.loadigouser.ui.user.UserActivity
import com.mywork.loadigouser.util.LocalNotificationType
import com.mywork.loadigouser.util.Resource
import com.mywork.loadigouser.util.ServiceType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : BaseFragment(), ServicesAdapter.ClickListener {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var user: User

    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var adapter: ServicesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch { viewModel.getServices() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.lifecycleOwner = this
        binding.adapter = adapter

        binding.user = user
        adapter.setClickListener(this)
        observeLiveData()

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.main_img))
        imageList.add(SlideModel(R.drawable.onboard1))
        imageList.add(SlideModel(R.drawable.onboard2))
        imageList.add(SlideModel(R.drawable.onboard3))
        imageList.add(SlideModel(R.drawable.ic_order_success))
        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT)

    }

    override fun onResume() {
        super.onResume()
        (activity as UserActivity).binding.iHeader.tvTitle.text = getString(R.string.home)
        (activity as UserActivity).binding.iHeader.btnBack.visibility = View.GONE
        (activity as UserActivity).binding.iHeader.clHeader.visibility = View.VISIBLE
        (activity as UserActivity).binding.iHeader.btnBell.visibility = View.VISIBLE
    }


    private fun observeLiveData() {
        viewModel.servicesLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
//                    showLoadingIndicator()
                }
                is Resource.Success -> {
                    hideLoadingIndicator()
                    adapter.submitList(response.data)
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

    override fun onClickService(position: Int) {
        val service = adapter.currentList[position]
        if (service.name == ServiceType.TOW_TRUCK.serviceName)
            navController.navigate(R.id.action_mainFragment_to_towTruckFragment)
        else {
            val action = MainFragmentDirections.actionMainFragmentToCategoriesFragment(service)
            navController.navigate(action)
        }
    }
}