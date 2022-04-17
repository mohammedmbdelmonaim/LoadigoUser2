package com.mywork.loadigouser.ui.user.profile.help

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
import com.mywork.loadigouser.databinding.FragmentHelpBinding
import com.mywork.loadigouser.databinding.FragmentNotificationsBinding
import com.mywork.loadigouser.model.remote.request.auth.OtpRequest
import com.mywork.loadigouser.model.remote.response.GetHelpModel
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
class HelpFragment : BaseFragment(){
    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private val viewModel: HelpViewModel by viewModels()

    @Inject
    lateinit var questionsAdapter: QuestionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_help, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        lifecycleScope.launch { viewModel.getCategoriesByServiceId(args.service.id!!)}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as UserActivity).binding.iHeader.tvTitle.text = getString(R.string.help)
        (activity as UserActivity).binding.iHeader.btnBack.visibility = View.VISIBLE
        (activity as UserActivity).binding.iHeader.clHeader.visibility = View.VISIBLE
        (activity as UserActivity).binding.iHeader.btnBell.visibility = View.VISIBLE
        navController = Navigation.findNavController(view)
        binding.lifecycleOwner = this
        binding.adapter = questionsAdapter
        observeLiveData()
    }

    override fun onStart() {
        super.onStart()
        val listOfQues = arrayListOf<GetHelpModel>()
        for (i in 0 .. 5){
            listOfQues.add(GetHelpModel(i,"What is Lorem Ipsum?" , "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text " , false))
        }
        questionsAdapter.submitList(listOfQues)
    }


    private fun observeLiveData() {
        viewModel.categoriesLiveData.observe(viewLifecycleOwner) { response ->
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