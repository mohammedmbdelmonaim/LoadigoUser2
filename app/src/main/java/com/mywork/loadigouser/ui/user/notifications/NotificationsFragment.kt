package com.mywork.loadigouser.ui.user.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.mywork.loadigouser.R
import com.mywork.loadigouser.base.BaseFragment
import com.mywork.loadigouser.databinding.FragmentCategoriesBinding
import com.mywork.loadigouser.databinding.FragmentNotificationsBinding
import com.mywork.loadigouser.model.remote.request.auth.OtpRequest
import com.mywork.loadigouser.model.remote.response.user.CategoriesResponse
import com.mywork.loadigouser.model.remote.response.user.ServiceResponse
import com.mywork.loadigouser.ui.auth.complete.CompleteFragmentArgs
import com.mywork.loadigouser.ui.user.UserActivity
import com.mywork.loadigouser.ui.user.main.MainFragmentDirections
import com.mywork.loadigouser.util.LocalNotificationType
import com.mywork.loadigouser.util.Resource
import com.mywork.loadigouser.util.ServiceType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationsFragment : BaseFragment(){
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private val viewModel: NotificationsViewModel by viewModels()

    @Inject
    lateinit var adapter: NotificationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_notifications, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        lifecycleScope.launch { viewModel.getCategoriesByServiceId(args.service.id!!)}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as UserActivity).binding.iHeader.tvTitle.text = getString(R.string.notifications)
        (activity as UserActivity).binding.iHeader.btnBack.visibility = View.VISIBLE
        (activity as UserActivity).binding.iHeader.clHeader.visibility = View.VISIBLE
        (activity as UserActivity).binding.iHeader.btnBell.visibility = View.VISIBLE
        navController = Navigation.findNavController(view)
        binding.lifecycleOwner = this
        binding.adapter = adapter
        observeLiveData()
    }


    private fun observeLiveData() {
        viewModel.categoriesLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoadingIndicator()
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
}