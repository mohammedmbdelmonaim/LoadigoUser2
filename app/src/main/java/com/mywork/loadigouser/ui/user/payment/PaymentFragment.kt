package com.mywork.loadigouser.ui.user.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.mywork.loadigouser.R
import com.mywork.loadigouser.base.BaseFragment
import com.mywork.loadigouser.databinding.FragmentPaymentBinding
import com.mywork.loadigouser.ui.user.UserActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PaymentFragment : BaseFragment() {
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
//    private val viewModel: MainViewModel by viewModels()
//
    @Inject
    lateinit var adapter: CardsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_payment, container, false)
        return binding.root
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        lifecycleScope.launch { viewModel.getServices()}
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.lifecycleOwner = this
        binding.adapter = adapter
//        adapter.setClickListener(this)
//        observeLiveData()

    }

    override fun onResume() {
        super.onResume()
        (activity as UserActivity).binding.iHeader.tvTitle.text = ""
        (activity as UserActivity).binding.iHeader.btnBack.visibility = View.VISIBLE
        (activity as UserActivity).binding.iHeader.btnBell.visibility = View.GONE

        binding.mcRefer.setOnClickListener {
            navController.navigate(R.id.action_paymentFragment_to_referralFragment)
        }
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