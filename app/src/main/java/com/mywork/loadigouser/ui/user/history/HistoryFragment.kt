package com.mywork.loadigouser.ui.user.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayoutMediator
import com.mywork.loadigouser.R
import com.mywork.loadigouser.base.BaseFragment
import com.mywork.loadigouser.databinding.FragmentCategoriesBinding
import com.mywork.loadigouser.databinding.FragmentHistoryBinding
import com.mywork.loadigouser.databinding.FragmentSuccessBinding
import com.mywork.loadigouser.model.remote.request.auth.OtpRequest
import com.mywork.loadigouser.model.remote.response.user.CategoriesResponse
import com.mywork.loadigouser.model.remote.response.user.ServiceResponse
import com.mywork.loadigouser.ui.user.UserActivity
import com.mywork.loadigouser.ui.user.history.completed.CompletedFragment
import com.mywork.loadigouser.ui.user.history.ongoing.OnGoingFragment
import com.mywork.loadigouser.util.LocalNotificationType
import com.mywork.loadigouser.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment : BaseFragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_history, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as UserActivity).binding.iHeader.clHeader.visibility = View.VISIBLE
        (activity as UserActivity).binding.iHeader.tvTitle.text = getString(R.string.my_booking)
        (activity as UserActivity).binding.iHeader.btnBack.visibility = View.VISIBLE
        (activity as UserActivity).binding.iHeader.btnBell.visibility = View.GONE
        navController = Navigation.findNavController(view)
        binding.lifecycleOwner = this
        setupFragmentsWithPager()
    }

    private fun setupFragmentsWithPager() {
        val categoriesNames = listOf(
            getString(R.string.ongoing),
            getString(R.string.past)
        )

        binding.vpHistory.adapter =
            SupportPagerAdapter(
                activity as AppCompatActivity,
                listOf(
                    OnGoingFragment(),
                    CompletedFragment(),
                )
            )

        TabLayoutMediator(binding.sectionsTabs, binding.vpHistory) { tab, position ->
            tab.text = categoriesNames[position]
            binding.vpHistory.setCurrentItem(tab.position, true)
        }.attach()


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}