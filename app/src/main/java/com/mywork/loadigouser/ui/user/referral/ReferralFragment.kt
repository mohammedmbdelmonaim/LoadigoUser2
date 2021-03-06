package com.mywork.loadigouser.ui.user.referral

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mywork.loadigouser.R
import com.mywork.loadigouser.base.BaseFragment
import com.mywork.loadigouser.databinding.FragmentPaymentBinding
import com.mywork.loadigouser.databinding.FragmentReferralBinding
import com.mywork.loadigouser.ui.user.UserActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReferralFragment : BaseFragment() {
    private var _binding: FragmentReferralBinding? = null
    private val binding get() = _binding!!

//    private lateinit var navController: NavController
//    private val viewModel: MainViewModel by viewModels()
//
//    @Inject
//    lateinit var adapter: ServicesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_referral, container, false)
        return binding.root
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        lifecycleScope.launch { viewModel.getServices()}
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        navController = Navigation.findNavController(view)
        binding.lifecycleOwner = this
//        binding.adapter = adapter
//        adapter.setClickListener(this)
//        observeLiveData()

    }

    override fun onResume() {
        super.onResume()
        (activity as UserActivity).binding.iHeader.tvTitle.text = ""
        (activity as UserActivity).binding.iHeader.btnBack.visibility = View.VISIBLE
    }


//    private fun observeLiveData() {
//        viewModel.servicesLiveData.observe(viewLifecycleOwner) { response ->
//            when (response) {
//                is Resource.Loading -> {
//                    showLoadingIndicator()
//                }
//                is Resource.Success -> {
//                    hideLoadingIndicator()
//                    adapter.submitList(response.data)
//                }
//
//                is Resource.Error -> {
//                    hideLoadingIndicator()
//                    showLocalNotification(
//                        LocalNotificationType.ERROR, response.message.toString()
//                    )
//                }
//                else -> hideLoadingIndicator()
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}