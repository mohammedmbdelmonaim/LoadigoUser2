package com.mywork.loadigouser.ui.auth.otp

import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
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
import com.mywork.loadigouser.databinding.FragmentOtpBinding
import com.mywork.loadigouser.databinding.FragmentRegisterBinding
import com.mywork.loadigouser.model.remote.request.auth.LoginRequest
import com.mywork.loadigouser.model.remote.request.auth.OtpRequest
import com.mywork.loadigouser.util.KeyboardUtils
import com.mywork.loadigouser.util.LocalNotificationType
import com.mywork.loadigouser.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OtpFragment : BaseFragment() {
    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private val viewModel: OtpViewModel by viewModels()

    private val args: OtpFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_otp, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.lifecycleOwner = this
        timer = createCountDownTimer(120000)
        timer.start()
        observeLiveData()
        handleClicks()
    }

    override fun onResume() {
        super.onResume()
        KeyboardUtils.showSoftKeyboard(requireContext(), this.binding.etOtp)
        if (msUntilFinished != 0L) {
            timer = createCountDownTimer(msUntilFinished)
            timer.start()
        }
    }

    private fun handleClicks() {
        binding.btnContinue.setOnClickListener {
            if (binding.etOtp.text.toString().length < 4) {
                showLocalNotification(
                    LocalNotificationType.ERROR, getString(R.string.insert_correct_otp)
                )
                return@setOnClickListener
            }
            lifecycleScope.launch { viewModel.checkOtp(OtpRequest(args.phone, binding.etOtp.text.toString())) }
        }

        binding.btnResend.setOnClickListener {
            binding.emptyViews.visibility = View.INVISIBLE
            binding.tvTimer.visibility = View.VISIBLE
            timer = createCountDownTimer(120000)
            this.timer.start()
        }
    }

    private fun observeLiveData() {
        viewModel.otpLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoadingIndicator()
                }
                is Resource.Success -> {
                    hideLoadingIndicator()
                    showLocalNotification(
                        LocalNotificationType.SUCCESS, response.message.toString()
                    )
                    val action = OtpFragmentDirections.actionOtpFragmentToCompleteFragment(args.phone)
                    navController.navigate(action)
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

    private lateinit var timer: CountDownTimer
    var msUntilFinished = 0L
    private fun createCountDownTimer(millisUntilFinished: Long) =

        object : CountDownTimer(millisUntilFinished, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                binding.tvTimer.text = String.format(getString(R.string.sec_left),
                    DateUtils.formatElapsedTime(millisUntilFinished.div(1000))
                )
                msUntilFinished = millisUntilFinished
            }

            override fun onFinish() {
                binding.emptyViews.visibility = View.VISIBLE
                binding.tvTimer.visibility = View.INVISIBLE
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        _binding = null
    }

}