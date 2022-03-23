package com.mywork.loadigouser.ui.auth.complete

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.navigateUp
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButton
import com.mywork.loadigouser.R
import com.mywork.loadigouser.base.BaseFragment
import com.mywork.loadigouser.databinding.FragmentCompleteBinding
import com.mywork.loadigouser.databinding.FragmentLoginBinding
import com.mywork.loadigouser.model.remote.request.auth.CompleteRequest
import com.mywork.loadigouser.model.remote.request.auth.LoginRequest
import com.mywork.loadigouser.ui.auth.AuthActivity
import com.mywork.loadigouser.ui.boarding.OnBoardingActivity
import com.mywork.loadigouser.ui.dialogs.DatePickerFragment
import com.mywork.loadigouser.ui.user.UserActivity
import com.mywork.loadigouser.util.Constants
import com.mywork.loadigouser.util.LocalNotificationType
import com.mywork.loadigouser.util.Resource
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.EasyPermissions.hasPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CompleteFragment : BaseFragment() {
    private var _binding: FragmentCompleteBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private val viewModel: CompleteViewModel by viewModels()
    private val args: CompleteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_complete, container, false)
        return binding.root
    }

    private var gender = Gender.MALE.name
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.lifecycleOwner = this
        binding.toggleButton.check(R.id.btnMale)
        handleToggleClicks()
        observeLiveData()
        handleClicks()
    }

    private fun handleToggleClicks() {
        binding.btnFemale.setOnClickListener {
            gender = Gender.FEMALE.name
        }

        binding.btnMale.setOnClickListener {
            gender = Gender.MALE.name
        }
    }

    private var birthDate: String? = null
    private fun handleClicks() {
        binding.btnFinish.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val fullName = binding.etFullName.text.toString()
            if (checkValidation(fullName, email)) {
                lifecycleScope.launch {
                    viewModel.completeUser(
                        CompleteRequest(
                            fullName,
                            email,
                            args.phone,
                            fileUri.toString(),
                            gender,
                            "2022-03-17T19:00:36.807Z"
                        )
                    )
                }
            }
        }

        this.binding.ivChangeImage.setOnClickListener {
            pickAnImage()
        }

        binding.tvBirthDate.setOnClickListener {
            openDOBDialog(birthDate)
        }
    }

    private fun openDOBDialog(currentDate: String?) {
        if (currentDate != null) {
            if (currentDate.isNotEmpty()) {
                val (day, month, year) = currentDate.split("-")
                this.createDialogWithoutDateField(
                    year = year.toInt(),
                    month = month.toInt() - 1,
                    day = day.toInt()
                )
            }
        } else {
            this.createDialogWithoutDateField()
        }
    }


    private fun createDialogWithoutDateField(
        year: Int = 0,
        month: Int = 0,
        day: Int = 0
    ) {
        DatePickerFragment(
            year, month, day,
            restrictPast = false,
            restrictFuture = true,
            birthdayPicker = true,
            sendItemDataListener = DatePickerFragment.SendSingleItemListener {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

                val calendar = Calendar.getInstance()

                val maxDateDate: Date = calendar.time
                val selectedData: Date? = sdf.parse("${it[0]}/${it[1]}/${it[2]}")

                if (selectedData != null) {
                    if (selectedData.before(maxDateDate)) {
                        birthDate = "${it[0]}-${it[1]}-${it[2]}"
                        binding.tvBirthDate.text = birthDate

                    } else {
                        showLocalNotification(
                            LocalNotificationType.ERROR, "من فضلك اختر التاريخ مره اخري"
                        )
                    }
                } else {
                    showLocalNotification(
                        LocalNotificationType.ERROR, "من فضلك اختر التاريخ مره اخري"
                    )
                }
            }, context_ = requireContext()
        ).show(this.childFragmentManager, "show_date")
    }

    private fun observeLiveData() {
        viewModel.completeLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoadingIndicator()
                }
                is Resource.Success -> {
                    hideLoadingIndicator()
                    navController.navigateUp()
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

    private fun checkValidation(fullName: String, email: String): Boolean {
        if (fullName.isNullOrEmpty()) {
            binding.etFullName.error = getString(R.string.fullname_is_required)
            return false
        }

        if (email.isNullOrEmpty()) {
            binding.etEmail.error = getString(R.string.email_is_required)
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = getString(R.string.email_not_valid)
            return false
        }
        if (birthDate.isNullOrEmpty()) {
            binding.tvBirthDate.error = getString(R.string.enter_birth)
            return false
        }
        if (fileUri == null || fileUri.toString().isEmpty()){
            showLocalNotification(
                LocalNotificationType.ERROR, getString(R.string.chose_your_profile)
            )
            return false
        }
        return true
    }

    var fileUri: Uri? = null
    private fun pickAnImage() {
        ImagePicker.with(requireActivity())
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start { resultCode, data ->
                when (resultCode) {
                    RESULT_OK -> {
                        if (data?.data?.path == null) {
                            showLocalNotification(
                                LocalNotificationType.ERROR,
                                ImagePicker.getError(data)
                            )
                            return@start
                        }
                        fileUri = data.data
                        this.binding.userImageView.setImageURI(fileUri)
                        this.binding.userImageView.visibility = View.VISIBLE
                    }
                    ImagePicker.RESULT_ERROR -> {
                        this.showLocalNotification(
                            LocalNotificationType.ERROR,
                            ImagePicker.getError(data)
                        )
                    }
                    else -> {
//                        this.showLocalNotification(
//                            LocalNotificationType.INFO,
//                            getString(R.string.task_cancelled)
//                        )
                    }
                }
            }
    }

    enum class Gender{
        MALE,FEMALE
    }
}