package com.mywork.loadigouser.ui.auth.otp

import androidx.lifecycle.ViewModel
import com.mywork.loadigouser.model.remote.request.auth.LoginRequest
import com.mywork.loadigouser.model.remote.request.auth.OtpRequest
import com.mywork.loadigouser.model.remote.response.auth.LoginResponse
import com.mywork.loadigouser.model.remote.response.auth.OtpResponse
import com.mywork.loadigouser.util.Resource
import com.mywork.loadigouser.util.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(private val repository: OtpRepository): ViewModel() {
    private val otpMutableLiveData = SingleLiveData<Resource<OtpResponse>>()
    val otpLiveData: SingleLiveData<Resource<OtpResponse>> get() = otpMutableLiveData

    suspend fun checkOtp(request: OtpRequest) {

        val response = repository.checkOtp(request = request)
        if (response.statusCode == 201) {
            otpMutableLiveData.value = Resource.Success(response.data!!,response.message!!)
        } else {
            otpMutableLiveData.value = Resource.Error(response.message!!)
        }
    }

}